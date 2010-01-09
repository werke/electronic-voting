package voter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import ssl.SSLManager;

public class VotingProcess implements VotingProcessInterface {

	private static final int PORT_NUMBER  = 3333;
	private Socket socket;
	private Logger vLogger;
	private boolean voterIsEligible;
	
	public VotingProcess()
	{
		try{
			SSLManager sslManager = new SSLManager("security/voters/Marius/Marius.ks", "Marius_password".toCharArray());
            socket = sslManager.connectSocket("localhost", PORT_NUMBER);
			voterIsEligible = false;
			vLogger =  Logger.getLogger("VotingLogger");			
			
		}catch(IOException exception){
			vLogger.error("Error intializing the client :" + exception.getMessage());
		}catch (Exception e){
			vLogger.error("Error intializing the client :" + e.getMessage());
		}
	}

	public void sendRequestToVote(Voter v) {
		
		try{
			//send the request
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(v);
			
			//reads the response
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Boolean response = (Boolean)ois.readObject();
			
			//closing connection
			ois.close();
			oos.close();
			socket.close();
			
			voterIsEligible = response.booleanValue();
			
		}catch(IOException exception){
			vLogger.error("Error sending the request for determining elligibility :" +exception.getMessage());
		}catch (ClassNotFoundException exception) {
			vLogger.error("Error sending the request for determing elligibility:" + exception.getMessage());
		}
	}
	
	public byte[] sendBlindedMessage(byte[] message) {
		try{
			//send the request
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			
			//reads the response
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			byte[] response = (byte[])ois.readObject();
			
			//closing connection
			ois.close();
			oos.close();
			socket.close();
			
			return response;
			
		}catch(IOException exception){
			vLogger.error("Error sending the request for vote validation :" +exception.getMessage());
		}catch (ClassNotFoundException exception) {
			vLogger.error("Error sending the request for vote validation:" + exception.getMessage());
		}
		return null;
	}

	public boolean voterIsEligible() {
		return voterIsEligible;
	}

	
}
