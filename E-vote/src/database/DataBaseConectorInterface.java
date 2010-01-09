package database;

import java.sql.Connection;

public interface DataBaseConectorInterface {

	public Connection getDatabaseConection (String url, String username, String password);

}
