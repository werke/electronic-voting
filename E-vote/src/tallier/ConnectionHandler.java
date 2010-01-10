package tallier;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import database.DataBaseConector;

import ssl.RSA_Blinder;
import ssl.SSLManager;
import utils.Ballot;
import utils.Candidate;
import voter.Voter;

public class ConnectionHandler implements Runnable {
	
	private Socket socket;
	private Logger vLogger = Logger.getLogger("TallierLogger");
	private RSAPublicKey publicKey;
	
	public ConnectionHandler(Socket socket , RSAPublicKey key){
		this.socket = socket;
		this.publicKey = key;
	}

	@Override
	public void run() {

		try{			
			//read the unblinded signed vote received from voter
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			byte[] vote = (byte[])ois.readObject();
			vLogger.info("We got one vote , checking it");
			
			//unsign the message get			
			byte[] unsignedMessage = RSA_Blinder.unsign(vote , publicKey);
			
			Ballot bVote = Ballot.fromByteArray(unsignedMessage);
			vLogger.info("Vote valid , registering it in the database");
			//System.out.println(bVote.toString());
			//System.out.println(getCandidate(Integer.parseInt(bVote.toString())));
			
			registerVoteDatabase(getCandidate(Integer.parseInt(bVote.toString())));
			vLogger.info("Vote registered in the database");
			
			writeRegisteredVotesToFile(getCandidate(Integer.parseInt(bVote.toString())));
			vLogger.info("Vote registered in the file for future verifications");
			
			//write the response for vote confirmation
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(true);
			vLogger.info("Vote confirmed to the voter");
	
			oos.flush();
			
			
			//close connection
			ois.close();
			oos.close();
			socket.close();
			
		}catch (Exception exception){
			vLogger.error("Error in managing the vote  , possible fraud detection :" + exception.getMessage());
			exception.printStackTrace();
		}
		
		
	}
	
	private Candidate getCandidate(int ID){
		
		DataBaseConector dbc = new DataBaseConector();
        Connection conn =  dbc.getDatabaseConection("jdbc:mysql://localhost:3306/mysql", "root", "");
        Statement stmt;
        ResultSet rs;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT VOTE_OPTION_ID , CANDIDATE , ORGANIZATION FROM evote.voting_options WHERE VOTE_OPTION_ID = "+ID);
            
            if (rs.first())
            	return new Candidate(rs.getInt("VOTE_OPTION_ID"), rs.getString("CANDIDATE"), rs.getString("ORGANIZATION"));
            else
            	return null;
            
        } catch (SQLException e) {
            vLogger.error("Error getting the connection to the database for extracting the voted candidate"+e.getMessage());
        }
        return null;
	}
	
	private void registerVoteDatabase(Candidate c){
		DataBaseConector dbc = new DataBaseConector();
        Connection conn =  dbc.getDatabaseConection("jdbc:mysql://localhost:3306/mysql", "root", "");
        Statement stmt;
        
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO evote.registered_votes (REGISTER_DATE , VOTE_REGISTERED) VALUES (SYSDATE() , "+c.getVoteOptionID()+")");
            
        } catch (SQLException e) {
            vLogger.error("Error getting the connection to the database for inserting the vote"+e.getMessage());
        }
	}
	
	private void writeRegisteredVotesToFile(Candidate c){
		Logger voteLogger = Logger.getLogger("VoterLogger");
		voteLogger.info("Vote registered for candidate "+ c.getCandidateName()+" from organization "+c.getOrganizationName());
		
		
	}
}
