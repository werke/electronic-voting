package validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import ssl.RSA_Blinder;
import utils.Voter;
import database.*;


public class ConnectionHandler implements Runnable{

	private Socket socket;
	private Logger vLogger = Logger.getLogger("ValidatorLogger");
	private RSAPrivateKey rsapvK;

	public ConnectionHandler(Socket socket , RSAPrivateKey rsapvK){
		this.socket = socket;
		this.rsapvK = rsapvK;
	}

	@Override
	public void run() {
		
		try{			
			//read the request for elligibility
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Voter voterRequest = (Voter)ois.readObject();
			vLogger.info("We got vote request from person identified by CNP "+voterRequest.getCNP());
			
			boolean ok = isEligibleToVote(voterRequest);
			
			//write the response for elligibility and send it
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(ok);
			vLogger.info("Response for elligibility sent " + ok);
			
			if (!ok){			
				//close connection
				ois.close();
				oos.close();
				socket.close();
				return;
			}
			oos.flush();

			//reads the blinded message
			ObjectInputStream ois1 = new ObjectInputStream(socket.getInputStream());
			byte[] blindedMesage = (byte[])ois1.readObject();
			vLogger.info("Received the following blinded message :  "+blindedMesage.toString());
			
			//we sign the message with validator private key
			ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
			
			setVoterNotEligible(voterRequest);
			vLogger.info("Voter registered in database");
			
			oos1.writeObject(RSA_Blinder.sign(blindedMesage, rsapvK));
			vLogger.info("Response with the blinded message signed was sent successfully");
			
			
			
			
			//close connection
			ois.close();
			ois1.close();
			oos.close();
			oos1.close();
			socket.close();
			
			
		}catch (Exception exception){
			vLogger.error("Error managing a client request :" + exception.getMessage());
			exception.printStackTrace();
		}
	
		
		
	}
	
	public boolean isEligibleToVote(Voter v){
		DataBaseConector dbc = new DataBaseConector();
		Connection conn =  dbc.getDatabaseConection("jdbc:mysql://localhost:3306/mysql", "root", "");
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			String voterCNP = v.getCNP();
			rs = stmt.executeQuery("SELECT VOTED FROM evote.eligible_voters where CNP='"+voterCNP+"'");
			if (!rs.first()){
				return false;
			}
			return !rs.getBoolean("VOTED");
			
		} catch (SQLException e) {
			vLogger.error("Error getting the connection to the database "+e.getMessage());
			return false;
		}
	}
	private void setVoterNotEligible(Voter v){
		DataBaseConector dbc = new DataBaseConector();
		Connection conn =  dbc.getDatabaseConection("jdbc:mysql://localhost:3306/mysql", "root", "");
		Statement stmt;
		
		try {
			stmt = conn.createStatement();
			String voterCNP = v.getCNP();
			stmt.executeUpdate("UPDATE evote.eligible_voters SET VOTED=1 WHERE CNP='"+v.getCNP()+"'");
			
		} catch (SQLException e) {
			vLogger.error("Error getting the connection to the database "+e.getMessage());
		}
	}
	
}
