package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;

public class DataBaseConector implements DataBaseConectorInterface {
	Logger dataBaseCoonectorLogger = null;
	
	public DataBaseConector()
	{
		try {
            dataBaseCoonectorLogger = Logger.getLogger("DBLogger");
            File f = new File("logs", "db.log");
            FileAppender fapp = new FileAppender(new TTCCLayout("DATE"), f.getAbsolutePath());
            dataBaseCoonectorLogger.addAppender(fapp);
        } catch( IOException e ) {
            System.err.println("Failed to initialize logging for database");
            return;
        }
	}

	public Connection getDatabaseConection(String url, String username,String password) {
		try{
			return DriverManager.getConnection(url , username , password);
		}
		catch (SQLException e)
		{
			if (dataBaseCoonectorLogger != null	){
				dataBaseCoonectorLogger.error("Error getting the connection to the database : "+ e.getMessage());
			}
			else{
				System.err.println("Error getting the connection to the database : "+ e.getMessage());
			}
		}
		return null;
	}
	
	public void initializeLogger() {
		
		
	}
	

}
