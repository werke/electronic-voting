package ssl;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;

import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

public class SSLManager {

    // variabilele pentru configurarea SSL
    private KeyStore keystore;
    private KeyManagerFactory keyManagerFactory;
    private TrustManagerFactory trustManagerFactory;
    private SSLContext sslContext;
    private Certificate CACertificate;

    public SSLManager( String keystorePath, char [] keystorePass )
            throws Exception
    {
        initSSL( keystorePath, keystorePass );
    }

    private void initSSL( String keystorePath, char[] keystorePass )
            throws Exception {
        this.keystore = KeyStore.getInstance("JKS");
        this.keystore.load( new FileInputStream(keystorePath), keystorePass);

        this.keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        this.keyManagerFactory.init(keystore, keystorePass);

        this.trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        this.trustManagerFactory.init(keystore);

        this.sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        this.CACertificate = keystore.getCertificate("certification_authority");
    }

    public SSLSocket connectSocket( String host, int port )
            throws IOException {
        SSLSocket socket = (SSLSocket)
                (sslContext.getSocketFactory().createSocket(host, port));
        socket.startHandshake();

        // conectarea la document server si verificarea autenticitatii acestuia
        try {
            X509Certificate[] serverCertificates =
                    (X509Certificate[])(socket.getSession()).getPeerCertificates();
            if( !CACertificate.equals(serverCertificates[1]))
                throw new Exception("bad server certificate");
            else System.out.println("Server certificate ok");
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return socket;
    }

    public SSLServerSocket initServerSocket( int port )
            throws IOException{
        SSLServerSocket sSocket =
                (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port);
        sSocket.setNeedClientAuth(true);
        return sSocket;
    }
}
