package ssl;

import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;

import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

public class Client {

    private String name;


    // variabilele pentru configurarea SSL
    private KeyStore keystore;
    private KeyManagerFactory keyManagerFactory;
    private TrustManagerFactory trustManagerFactory;
    private SSLContext sslContext;
    private Certificate CACertificate;
    // socketul pentru comunicarea cu serverul
    private SSLSocket docSocket;

    public Client( String clientName, String docServerHost, int docServerPort )
            throws Exception {
        this.name = clientName;
        String keystorePath = "users/"+name+"/security/"+name+".ks";
        char[] keystorePass = new String(name+"_password").toCharArray();
        initSSL( keystorePath, keystorePass );
        this.docSocket = connectSocket( docServerHost, docServerPort );
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

    private SSLSocket connectSocket( String host, int port )
            throws IOException {
        SSLSocket socket = (SSLSocket)
                (sslContext.getSocketFactory().createSocket(host, port));
        socket.startHandshake();

        // conectarea la document server si verificarea autenticitatii acestuia
        try {
            X509Certificate[] serverCertificates =
                    (X509Certificate[])((this.docSocket).getSession()).getPeerCertificates();
            if( !CACertificate.equals(serverCertificates[1]))
                throw new Exception("bad server certificate");
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return socket;
    }
}
