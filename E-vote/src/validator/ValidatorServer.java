package validator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class ValidatorServer {
	
	private final static int PORT_NUMBER = 3333;
	private ServerSocket server;
	private Logger vLogger = new ValidatorLogger().getValidatorLogger();
	
	public ValidatorServer(){

		try{
			server = new ServerSocket(PORT_NUMBER);
			Security.addProvider(new BouncyCastleProvider());
			try {
				Signature sig = Signature.getInstance("SHA1WithRSA", "BC");
				System.out.println(sig.toString());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch(IOException exception){
			vLogger.error("Error initializing the validator : "+ exception.getMessage());
		}
	}
	
	public void handleIncomingConnections(){
		vLogger.info("Server started . Waiting for incoming connections...");
		ExecutorService pool = Executors.newFixedThreadPool(32);
		while (true){
			try{
				Socket socket = server.accept();
				pool.execute(new ConnectionHandler(socket));
			}catch (IOException exception){
				vLogger.error("Error in accepting the connection : "+exception.getMessage());
			}
		}
	}
}

	