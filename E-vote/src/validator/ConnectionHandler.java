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
			//read the request
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Voter voterRequest = (Voter)ois.readObject();
			vLogger.info("Am primit cerere de vot din partea persoanei cu CNP "+voterRequest.getCNP());
			
			boolean ok = isEligibleToVote(voterRequest);
			
			//write the response and send it
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(ok);
			vLogger.info("Response for elligibility sent " + ok);
			
			//close connection
			ois.close();
			oos.close();
			socket.close();
			
			
		}catch (IOException exception){
			vLogger.error("Error managing a client request :" + exception.getMessage());
		}catch (ClassNotFoundException exception) {
			vLogger.error("Error managing a client request :" + exception.getMessage());
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
