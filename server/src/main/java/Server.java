import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Created by krishnaraj on 24/2/16.
 */
public class Server {
    public static final int PORT = 4646;
    TrustManagerFactory tmf;
    KeyManagerFactory kmf;
    KeyStore publicKeyStore;
    KeyStore privateKeyStore;
    SSLServerSocket serverSocket;

    public static void main(String args[]) {
        Server server = new Server();
        server.init();
    }

    private void init() {
        InputStream privateKeyStoreIns;
        InputStream publicKeyStoreIns;

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextInt();

        privateKeyStoreIns = Server.class.getResourceAsStream("server.private");
        publicKeyStoreIns = Server.class.getResourceAsStream("client.public");

        Security.addProvider(new BouncyCastleProvider());

        try {
            privateKeyStore = setupKeystore(privateKeyStoreIns, "private");
            publicKeyStore = setupKeystore(publicKeyStoreIns, "public");

            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(publicKeyStore);

            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(privateKeyStore, "private".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(),
                    tmf.getTrustManagers(),
                    secureRandom);

            SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
            serverSocket = (SSLServerSocket) sf.createServerSocket( PORT );
            serverSocket.setNeedClientAuth(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private KeyStore setupKeystore(InputStream keyStoreInputStream, String passphrase)
            throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(keyStoreInputStream, passphrase.toCharArray());

        return keyStore;
    }

}
