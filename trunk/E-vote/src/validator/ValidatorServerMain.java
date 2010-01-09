package validator;

public class ValidatorServerMain {

	public static void main (String [] args){
		
		ValidatorServer vs = new ValidatorServer();
		vs.handleIncomingConnections();
	}
}
