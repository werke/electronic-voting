package tallier;

import utils.MyLogger;

public class TallierServerMain {
	
public static void main (String [] args){
        MyLogger.initLogging("TallierLogger", "tallier.log");
        MyLogger.initLogging("VoterLogger", "voterCountVerification.log");
        TallierServer vs = new TallierServer();
        new Thread(vs).start();
    }

}
