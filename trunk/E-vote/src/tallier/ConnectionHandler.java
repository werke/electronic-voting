package tallier;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.log4j.Logger;

import ssl.RSA_Blinder;
import utils.Ballot;
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
			vLogger.info("Am primit un vot");
			
			//unsign the message get			
			byte[] unsignedMessage = RSA_Blinder.unsign(vote , publicKey);
			
			Ballot bVote = Ballot.fromByteArray(unsignedMessage);
			System.out.println(bVote.toString());
			
			
			//write the response for vote confirmation
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(true);
			vLogger.info("Vote confirmed to the voter" + true);
	
			oos.flush();
			
			
			//close connection
			ois.close();
			oos.close();
			socket.close();
			
		}catch (Exception exception){
			vLogger.error("Error managing a client request :" + exception.getMessage());
			exception.printStackTrace();
		}
		
		
	}

}
