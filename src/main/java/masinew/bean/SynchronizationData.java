package masinew.bean;

public class SynchronizationData {
	private int syncId;
	private String dbUser;
	private String dbPassword;
	private String dbDriver;
	private String dbUrl;
	private String sqlStatement;
	private String sqlMarkedReceived;
	
	public SynchronizationData() { }
	
	public SynchronizationData(int syncId, String dbUser, String dbPassword, String dbDriver, String dbUrl, String sqlStatement, String sqlMarkedReceived) {
		this.syncId = syncId;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.sqlStatement = sqlStatement;
		this.sqlMarkedReceived = sqlMarkedReceived;
	}
	
	public int getSyncId() {
		return syncId;
	}

	public void setSyncId(int syncId) {
		this.syncId = syncId;
	}

	public String getDbUser() {
		return dbUser;
	}
	
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getDbDriver() {
		return dbDriver;
	}
	
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}
	
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getSqlStatement() {
		return sqlStatement;
	}

	public void setSqlStatement(String sqlStatement) {
		this.sqlStatement = sqlStatement;
	}

	public String getSqlMarkedReceived() {
		return sqlMarkedReceived;
	}

	public void setSqlMarkedReceived(String sqlMarkedReceived) {
		this.sqlMarkedReceived = sqlMarkedReceived;
	}

	@Override
	public String toString() {
		return "SynchronizationData [syncId=" + syncId + ", dbUser=" + dbUser + ", dbPassword=" + dbPassword
				+ ", dbDriver=" + dbDriver + ", dbUrl=" + dbUrl + ", sqlStatement=" + sqlStatement
				+ ", sqlMarkedReceived=" + sqlMarkedReceived + "]";
	}
}
