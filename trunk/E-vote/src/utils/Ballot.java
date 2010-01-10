package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;

public class Ballot implements Serializable {

    private final int       vote_option_id;
    private final String    candidate;
    private final String    organization;
    private final byte []   randomBytes;

    public Ballot( int vote_option_id, String candidate, String organization ) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
        this.randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        this.vote_option_id = vote_option_id;
        this.candidate = candidate;
        this.organization = organization;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getOrganization() {
        return organization;
    }

    public int getVote_option_id() {
        return vote_option_id;
    }
    
//    public Ballot(byte [] raw) {
//        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
//        ObjectInputStream ois = new ObjectInputStream(bais);
//
//    }

    public byte [] getBytes() {
        byte[] ret = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            ret = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing Ballot to byte array.", e);
        }
        return ret;
    }
}
