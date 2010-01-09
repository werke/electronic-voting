package voter;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class Voter implements VoterInterface {

	public Voter()
	{
		try {
            Logger dataBaseCoonectorLogger = Logger.getLogger("VoterLogger");
            File f = new File("logs", "voter.log");
            FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
            dataBaseCoonectorLogger.addAppender(fapp);
        } catch( IOException e ) {
            System.err.println("Failed to initialize logging for voter");
            return;
        }
	}
}
