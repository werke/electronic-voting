package utils;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import ssl.RSA_Blinder;

public class Ballot {

    private final int       vote_option_id;
    private final int       random;

    public Ballot( int vote_option_id ) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
        this.vote_option_id = vote_option_id;
        this.random = random.nextInt();
    }

    private Ballot( int vote_option_id, int random ) {
        this.vote_option_id = vote_option_id;
        this.random = random;
    }

    public static Ballot fromByteArray(byte [] raw)
            throws ClassNotFoundException, IOException
    {
        String str = new String(raw);
        String [] parts = str.split(" ");
        return new Ballot(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public int getVote_option_id() {
        return vote_option_id;
    }

    public byte [] getBytes() {
        return ("" + vote_option_id + " " + random).getBytes();
    }

    @Override
    public String toString() {
        return new String( "" + vote_option_id );
    }
}
