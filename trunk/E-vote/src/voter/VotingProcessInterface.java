package voter;

import utils.Voter;

public interface VotingProcessInterface {

		public void sendRequestToVote(Voter v);
		public byte[] sendBlindedMessage(byte[] message);

}
