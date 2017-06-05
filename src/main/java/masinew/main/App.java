package masinew.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;

import masinew.services.DB2Service;

public class App {
	private Connection connect = null;
	private WorkerThread worker;
	
    public static void main( String[] args ) {
    	// Initialize
    	final App app = new App();
    	
        // Read system properties
    	Properties systemProp = app.getPropertyFile("system.properties");
    	if (systemProp == null) {
    		System.err.println("Can not get System Property");
    		System.exit(1);
    	}
    	
    	// Connect Database
    	app.connectDatabase(systemProp);
    	DB2Service.initial(app.connect);
    	
    	// Worker
    	app.worker = new WorkerThread(app.connect, "MainWorker");
    	app.worker.start();
    	
    	// Hook Termination
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        app.closeDatabaseConnection();
		        app.worker.stopWorking();
		    }
		});
    }
    
    private void closeDatabaseConnection() {
    	try {
			if(connect != null){
				if (connect.isClosed()) {
					connect.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private Connection connectDatabase(Properties systemProp) {
    	String dbUser = systemProp.getProperty("db.user");
    	String dbPassword = systemProp.getProperty("db.password");
    	String dbUrl = systemProp.getProperty("db.url");
    	String dbDriver = systemProp.getProperty("db.driver");
    	
    	System.out.println(dbUser);
    	System.out.println(dbPassword);
    	System.out.println(dbUrl);
    	System.out.println(dbDriver);
    	boolean dbAuthIsEncode = Boolean.parseBoolean(systemProp.getProperty("db.authen.isEncode"));
    	if (dbAuthIsEncode) {
    		dbUser = new String(Base64.getDecoder().decode(dbUser));
    		dbPassword = new String(Base64.getDecoder().decode(dbPassword));
    	}
    	
		try {
			Class.forName(dbDriver);
			connect =  DriverManager.getConnection(dbUrl, dbUser, dbPassword);
			connect.setAutoCommit(false);
			return connect;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
    }
    
    private Properties getPropertyFile(String propertyFileName) {
		InputStream input = null;
		try {
			input = new FileInputStream(propertyFileName);
		} catch (FileNotFoundException e) {
			return null;
		}
		
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (IOException e) {
			return null;
		}
		
		return prop;
	}   
}
