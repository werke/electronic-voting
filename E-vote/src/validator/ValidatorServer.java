package validator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import ssl.SSLManager;
import utils.MyLogger;


public class ValidatorServer {
	
	private final static int PORT_NUMBER = 3333;
	private ServerSocket server;
	private Logger vLogger = Logger.getLogger("ValidatorLogger");
	
	public ValidatorServer(){

		try{
			
			SSLManager sslManager = new SSLManager("security/Validator/Validator.ks", "validator_password".toCharArray());
            server = sslManager.initServerSocket(PORT_NUMBER);           
			
		}catch(IOException exception){
			vLogger.error("Error initializing the validator : "+ exception.getMessage());
		}catch(Exception e){
			vLogger.error("SSL manager error : "+ e.getMessage());
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

	