package voter;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class VoterLogger {
	Logger voterLogger = null;

	public VoterLogger(){
		try {
	        voterLogger = Logger.getLogger("VoterLogger");
	        File f = new File("logs", "voter.log");
	        FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
	        voterLogger.addAppender(fapp);
	    } catch( IOException e ) {
	        System.err.println("Failed to initialize logging for voter");
	        return;
	    }
	}

	public Logger getVoterLogger() {
		return voterLogger;
	}

	public void setVoterLogger(Logger voterLogger) {
		this.voterLogger = voterLogger;
	}
	
}
