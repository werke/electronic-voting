package validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import voter.Voter;
import database.*;


public class ConnectionHandler implements Runnable{

	private Socket socket;
	private Logger vLogger = Logger.getLogger("ValidatorLogger");

	public ConnectionHandler(Socket socket ){
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try{			
			//read the request for elligibility
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Voter voterRequest = (Voter)ois.readObject();
			vLogger.info("Am primit cerere de vot din partea persoanei cu CNP "+voterRequest.getCNP());
			
			boolean ok = isEligibleToVote(voterRequest);
			
			//write the response for elligibility and send it
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(ok);
			vLogger.info("Response for elligibility sent " + ok);
			
			System.out.println("mata-1");
			if (!ok){			
				//close connection
				ois.close();
				oos.close();
				socket.close();
				return;
			}
			oos.flush();

			System.out.println("mata");
			//reads the blinded message
			ObjectInputStream ois1 = new ObjectInputStream(socket.getInputStream());
			byte[] blindedMesage = (byte[])ois1.readObject();
			vLogger.info("Am primit mesajul blinded "+blindedMesage.toString());
			
			
			
			//close connection
			ois.close();
			ois1.close();
			oos.close();
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
	
	
}
