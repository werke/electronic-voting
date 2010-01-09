package voter;

import org.apache.log4j.Logger;

public class VoterMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Voter v = new Voter("Ionut", "Posea","1851220081660");
		VotingProcess vp = new VotingProcess();
		Logger vLogger = new VoterLogger().getVoterLogger();
		vp.sendRequestToVote(v);
		if (!vp.isEligible())
			vLogger.fatal("Voter with CNP "+v.getCNP()+" is not eligible to vote. Possible fraud detected.");
		else
			vp.sendBlindedMessage("sdfsdf".getBytes());
			

	}

}
