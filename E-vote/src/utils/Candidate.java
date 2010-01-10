package utils;

public class Candidate {
	
	private int voteOptionID;
	private String candidateName;
	private String organizationName;
	
	public Candidate(int _voteOptionID ,String _candidateName , String _organizationName){
		this.voteOptionID = _voteOptionID;
		this.candidateName = _candidateName;
		this.organizationName = _organizationName;
	}
	
	public int getVoteOptionID() {
		return voteOptionID;
	}
	public void setVoteOptionID(int voteOptionID) {
		this.voteOptionID = voteOptionID;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	public String toString()
	{
		return "Candidate "+ candidateName + " from organization "+ organizationName;
	}

}
