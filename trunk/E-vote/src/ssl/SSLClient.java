package ssl;

import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Marius Ion
 */
public class SSLClient {

    public static void main(String [] args) {
        try {
            SSLManager sslManager = new SSLManager("security/voters/Marius/Marius.ks", "Marius_password".toCharArray());
            Socket sock = sslManager.connectSocket("localhost", 6000);
            PrintStream out = new PrintStream(sock.getOutputStream());
            out.println("Salut");
            sock.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
