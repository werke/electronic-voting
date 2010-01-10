package tallier;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;

import org.apache.log4j.Logger;

import ssl.RSA_Blinder;
import voter.Voter;

public class ConnectionHandler implements Runnable {
	
	private Socket socket;
	private Logger vLogger = Logger.getLogger("TallierLogger");

	public ConnectionHandler(Socket socket){
		this.socket = socket;
	}

	@Override
	public void run() {

		try{	/*		
			//read the request for elligibility
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			Voter voterRequest = (Voter)ois.readObject();
			vLogger.info("Am primit cerere de vot din partea persoanei cu CNP "+voterRequest.getCNP());
			
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
			vLogger.info("Am primit mesajul blinded "+blindedMesage.toString());
			
			//we sign the message with validator private key
			ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
			oos1.writeObject(RSA_Blinder.sign(blindedMesage, rsapvK));
			vLogger.info("Response with the blinded message signed was sent successfully");
			
			
			
			
			//close connection
			ois.close();
			ois1.close();
			oos.close();
			oos1.close();
			socket.close();
			*/
			
		}catch (Exception exception){
			vLogger.error("Error managing a client request :" + exception.getMessage());
			exception.printStackTrace();
		}
		
		
	}

}
