package ssl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Marius Ion
 */
public class SSLServer {

    public static void main(String [] args) {
        try {
            SSLManager sslManager = new SSLManager("security/Validator/Validator.ks", "validator_password".toCharArray());
            ServerSocket ssock = sslManager.initServerSocket(6000);

            Socket sock = ssock.accept();
            BufferedReader buf = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println(buf.readLine());

            sock.close();
            ssock.close();
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

}
