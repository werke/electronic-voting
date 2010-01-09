package validator;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class ValidatorLogger {
	Logger validatorLogger = null;

	
	public ValidatorLogger(){
		try {
            validatorLogger = Logger.getLogger("ValidatorLogger");
            File f = new File("logs", "validator.log");
            FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
            validatorLogger.addAppender(fapp);
        } catch( IOException e ) {
            System.err.println("Failed to initialize logging for validator");
            return;
        }
	}
	
	public Logger getValidatorLogger() {
		return validatorLogger;
	}

	public void setValidatorLogger(Logger validatorLogger) {
		this.validatorLogger = validatorLogger;
	}

}
