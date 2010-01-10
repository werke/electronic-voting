package tallier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import ssl.SSLManager;
import validator.ConnectionHandler;

public class TallierServer {

	private final static int PORT_NUMBER = 5555;
    private ServerSocket server;
    private Logger vLogger = Logger.getLogger("TallierLogger");
    private RSAPublicKey validatorPublicKey;
   // private RSAPrivateKey rsapvK;

    public TallierServer(){
        try{
            SSLManager sslManager = new SSLManager("security/Tallier/Tallier.ks", "tallier_password".toCharArray());
            server = sslManager.initServerSocket(PORT_NUMBER);
            validatorPublicKey = (RSAPublicKey)sslManager.getKeyStore().getCertificate("validator_private").getPublicKey();
        }catch(IOException exception){
            vLogger.error("Error initializing the tallier : "+ exception.getMessage());
        }catch(Exception e){
            vLogger.error("SSL manager error : "+ e.getMessage());
        }
    }
    public void handleIncomingConnections(){
        vLogger.info("Tallier Server started . Waiting for incoming connections...");
        ExecutorService pool = Executors.newFixedThreadPool(32);
        while (true){
            try{
                Socket socket = server.accept();
                pool.execute(new tallier.ConnectionHandler(socket ,validatorPublicKey ));
            }catch (IOException exception){
                vLogger.error("Error in accepting the connection : "+exception.getMessage());
            }
        }
    }
}
