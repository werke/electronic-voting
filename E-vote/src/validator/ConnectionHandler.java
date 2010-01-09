package validator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ConnectionHandler implements Runnable{

	private Socket socket;
	private Logger vLogger = new ValidatorLogger().getValidatorLogger();

	public ConnectionHandler(Socket socket ){
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try{			
			//read the request
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String request = (String)ois.readObject();
			vLogger.info("Am primit+"+request);
			
		}catch (IOException exception){
			vLogger.error("Error managing a client request :" + exception.getMessage());
		}catch (ClassNotFoundException exception) {
			vLogger.error("Error managing a client request :" + exception.getMessage());
		}
		
		
	}
	
	
}
