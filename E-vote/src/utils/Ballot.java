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

    public static Ballot fromByteArray(byte [] raw)
            throws ClassNotFoundException, IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Ballot)ois.readObject();
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

    public byte [] getBytes() {
        byte[] ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            ret = baos.toByteArray();
           
        } catch (IOException e) {
            throw new RuntimeException("Error serializing Ballot to byte array.", e);
        }finally{
        	 try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return ret;
    }

    @Override
    public String toString() {
        return new String( vote_option_id + " " + candidate + " " + organization );
    }
}
