package utils;

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
}
