package voter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;



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
