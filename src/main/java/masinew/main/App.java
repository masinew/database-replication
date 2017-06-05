package masinew.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import masinew.services.DB2Service;

public class App {
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
    	
    	// Connect Database ** OR use connectDatabaseAnaInitialReplicationSystem(Properties) method to do above methods together.
    	DB2Service.connectDatabase(systemProp);
    	DB2Service.initialReplicationSystem();
    	
    	// Worker
    	app.worker = new WorkerThread("MainWorker");
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
    	DB2Service.closeDatabase();
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
