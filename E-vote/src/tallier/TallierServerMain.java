package tallier;

import utils.MyLogger;
import validator.ValidatorServer;

public class TallierServerMain {
	
public static void main (String [] args){
		
		MyLogger.initLogging("TallierLogger", "tallier.log");
		TallierServer vs = new TallierServer();
		vs.handleIncomingConnections();
	}

}
