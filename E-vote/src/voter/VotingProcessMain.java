package voter;

import org.apache.log4j.Logger;
import utils.*;

public class VotingProcessMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Voter v = new Voter("Ionut", "Posea","1851220081660");
		VotingProcess vp = new VotingProcess();
		MyLogger.initLogging("VotingLogger", "voting.log");
		Logger vLogger = Logger.getLogger("VotingLogger");
		vp.sendRequestToVote(v);
		if (!vp.voterIsEligible()){
			vLogger.fatal("Voter with CNP "+v.getCNP()+" is NOT eligible to vote. Possible fraud detected.");
		}
		else{
			vLogger.info("Voter with CNP "+v.getCNP()+" is eligible to vote.");
			vp.sendBlindedMessage("sdfsdf".getBytes());
		}
			
			

	}

}
