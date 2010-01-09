package voter;

public interface VotingProcessInterface {

		public void sendRequestToVote(Voter v);
		public byte[] sendBlindedMessage(byte[] message);

}
