package validator;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class Validator implements ValidatorInterface {

	public Validator(){
		try {
            Logger dataBaseCoonectorLogger = Logger.getLogger("ValidatorLogger");
            File f = new File("logs", "validator.log");
            FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
            dataBaseCoonectorLogger.addAppender(fapp);
        } catch( IOException e ) {
            System.err.println("Failed to initialize logging for validator");
            return;
        }
	}
}
