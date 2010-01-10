package utils;

import java.io.Serializable;

public class Voter implements Serializable{

	private String firstName;
	private String lastName;
	private String CNP;
	
	public Voter(String firstName, String lastName, String cNP) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		CNP = cNP;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCNP() {
		return CNP;
	}
	public void setCNP(String cNP) {
		CNP = cNP;
	}
}
