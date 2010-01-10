package validator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

import ssl.SSLManager;


public class ValidatorServer implements Runnable {
	
    private final static int PORT_NUMBER = 3333;
    private ServerSocket server;
    private volatile boolean running;
    private Logger vLogger = Logger.getLogger("ValidatorLogger");
    private RSAPrivateKey rsapvK;

    public ValidatorServer(){
        try{
            SSLManager sslManager = new SSLManager("security/Validator/Validator.ks", "validator_password".toCharArray());
            server = sslManager.initServerSocket(PORT_NUMBER);
            server.setSoTimeout(1000);
            rsapvK = (RSAPrivateKey) sslManager.getKeyStore().getKey("validator_private", "validator_password".toCharArray());
        }catch(IOException exception){
            vLogger.error("Error initializing the validator : "+ exception.getMessage());
        }catch(Exception e){
            vLogger.error("SSL manager error : "+ e.getMessage());
        }
    }

    @Override
    public void run() {
        vLogger.info("Validator Server started . Waiting for incoming connections...");
        ExecutorService pool = Executors.newFixedThreadPool(32);
        this.running = true;
        vLogger.info("ValidatorServer started.");
        while (running){
            try{
                Socket socket = server.accept();
                pool.execute(new validator.ConnectionHandler(socket , rsapvK));
            } catch(SocketTimeoutException e ) {
                continue;
            } catch (IOException exception){
                vLogger.error("Error in accepting the connection : "+exception.getMessage());
            }
        }
        vLogger.info("ValidatorServer stopped.");
    }

    public void stop() {
        this.running = false;
    }
}

	