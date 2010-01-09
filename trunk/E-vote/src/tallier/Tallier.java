package tallier;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class Tallier implements TallierInterface{

	public Tallier()
	{
		try {
            Logger dataBaseCoonectorLogger = Logger.getLogger("TallierLogger");
            File f = new File("logs", "tallier.log");
            FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
            dataBaseCoonectorLogger.addAppender(fapp);
        } catch( IOException e ) {
            System.err.println("Failed to initialize logging for tallier");
            return;
        }
	}
}
