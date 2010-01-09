package validator;
import utils.*;

public class ValidatorServerMain {

	public static void main (String [] args){
		
		MyLogger.initLogging("ValidatorLogger", "validator.log");
		ValidatorServer vs = new ValidatorServer();
		vs.handleIncomingConnections();
	}
}
