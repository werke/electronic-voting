package validator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


public class ValidatorServer {
	
	private final static int PORT_NUMBER = 3333;
	private ServerSocket server;
	private Logger vLogger = new ValidatorLogger().getValidatorLogger();
	
	public ValidatorServer(){

		try{
			server = new ServerSocket(PORT_NUMBER);
			
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

	