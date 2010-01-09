package voter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;

import ssl.SSLManager;

public class VotingProcess implements VotingProcessInterface {

	private static final int PORT_NUMBER  = 3333;
	private Socket socket;
	private Logger vLogger;
	private boolean voterIsEligible;
	PublicKey pbK;
	
	public VotingProcess(String ksPath, char [] ksPass)
	{
		try{
			SSLManager sslManager = new SSLManager(ksPath, ksPass);
            socket = sslManager.connectSocket("localhost", PORT_NUMBER);
			voterIsEligible = false;
			vLogger =  Logger.getLogger("VotingLogger");			
			X509Certificate[] serverCertificates =(X509Certificate[])(((SSLSocket)socket).getSession()).getPeerCertificates();
			pbK = serverCertificates[0].getPublicKey();
			
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
			
			voterIsEligible = response.booleanValue();
			oos.flush();
			
		}catch(IOException exception){
			vLogger.error("Error sending the request for determining elligibility :" +exception.getMessage());
		}catch (ClassNotFoundException exception) {
			vLogger.error("Error sending the request for determing elligibility:" + exception.getMessage());
		}
	}
	
	public byte[] sendBlindedMessage(byte[] message) {
		try{
			ObjectOutputStream oosbm = new ObjectOutputStream(socket.getOutputStream());
			oosbm.writeObject(message);
			vLogger.info("Blinded nessage sent for verification and for signing");
			
			//reads the response
			ObjectInputStream oisbm = new ObjectInputStream(socket.getInputStream());
			byte[] response = (byte[])oisbm.readObject();
			vLogger.info("Blinded mesage signed received succsesfully");
			
			oosbm.flush();
			
			//closing connection
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

	public PublicKey getPbK() {
		return pbK;
	}

	
}
