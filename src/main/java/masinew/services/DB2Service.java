package masinew.services;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import masinew.bean.SynchronizationData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB2Service {
	public static void initial(Connection connect) {
		deleteAllSyncTrigger(connect);
		initializeTrigger(connect);
    }
	
	private static void deleteAllSyncTrigger(Connection connect) {
		try {
			Statement st = connect.createStatement();
			ResultSet syncTriggersQuery = st.executeQuery("SELECT TRIGNAME FROM SYSCAT.TRIGGERS WHERE TRIGNAME LIKE 'SYNC_%_TRIGGER'");
			while (syncTriggersQuery.next()) {
				String trigName = syncTriggersQuery.getString("TRIGNAME");
				Statement deleteSt = connect.createStatement();
				String deleteTriggerSql = "DROP TRIGGER " + trigName;
				deleteSt.execute(deleteTriggerSql);
				connect.commit();
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initializeTrigger(Connection connect) {
		try {
			Statement tableListSt = connect.createStatement();
			ResultSet tableListQuery = tableListSt.executeQuery("SELECT TABLENAME FROM SYNC_TABLE");
			while(tableListQuery.next()) {
				String tableName = tableListQuery.getString("TABLENAME").toUpperCase();
				initialInsertTrigger(connect, tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initialInsertTrigger(Connection connect, String tableName) {
		try {
			Statement tableStructureSt = connect.createStatement();
			ResultSet tableFieldIdQuery = tableStructureSt.executeQuery("SELECT COLNAMES FROM SYSIBM.SYSINDEXES WHERE TBNAME = '" + tableName + "'");
			String tableFieldId = tableName + "_ID";
			while(tableFieldIdQuery.next()) {
				String primaryKeyColNames = tableFieldIdQuery.getString("COLNAMES");
				String[] primaryKeyColNameArray = primaryKeyColNames.substring(1, primaryKeyColNames.length()).split("\\+");
				if (primaryKeyColNameArray.length == 1) {
					tableFieldId = primaryKeyColNameArray[0];
				}
			}
			
			ResultSet tableStructureQuery = tableStructureSt.executeQuery("SELECT DISTINCT(NAME), COLTYPE, LENGTH FROM SYSIBM.SYSCOLUMNS WHERE TBNAME = '" + tableName + "'");
			List<String> fieldList = new ArrayList<String>();
			
			while(tableStructureQuery.next()) {
				String fieldName = tableStructureQuery.getString("NAME");
				fieldList.add(fieldName);
			}
			
			String insertTrigger = DB2Template.getInsertAfterTriggerTemplate(tableName, tableFieldId, fieldList);
			String updateTrigger = DB2Template.getUpdateAfterTriggerTemplate(tableName, tableFieldId, fieldList);
			String deleteTrigger = DB2Template.getDeleteAfterTriggerTemplate(tableName, tableFieldId, fieldList);
			tableStructureSt.execute(insertTrigger);
			tableStructureSt.execute(updateTrigger);
			tableStructureSt.execute(deleteTrigger);
			connect.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<SynchronizationData> getSynchronizationData(Connection connect) {
		List<SynchronizationData> synchronizationDataList = new ArrayList<SynchronizationData>();
		try {
			Statement st = connect.createStatement();
			String sql = 	
					"SELECT SYNC.SYNC_INFORMATION_ID, SYNC.SQL_STATEMENT, SYNC.SQL_MARKED_RECEIVED, DB_INFO.DB_DRIVER, DB_INFO.DB_URL, DB_INFO.DB_USER, DB_INFO.DB_PASSWORD " +
					"FROM SYNC_INFORMATION SYNC " +
					"JOIN SYNC_DATABASE_INFORMATION DB_INFO ON SYNC.DATABASE_INFORMATION_ID = DB_INFO.DATABASE_INFORMATION_ID " +
					"WHERE SQL_SYNC_STATUS_ID IN (1, 3)";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				int syncId = rs.getInt("SYNC_INFORMATION_ID");
				String dbUser = rs.getString("DB_USER");
				String dbPassword = rs.getString("DB_PASSWORD");
				String dbDriver = rs.getString("DB_DRIVER");
				String dbUrl = rs.getString("DB_URL");
				String sqlStatement = rs.getString("SQL_STATEMENT");
				String sqlMarkedReceived = rs.getString("SQL_MARKED_RECEIVED");
				SynchronizationData syncData = new SynchronizationData(syncId, dbUser, dbPassword, dbDriver, dbUrl, sqlStatement, sqlMarkedReceived);
				synchronizationDataList.add(syncData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return synchronizationDataList;
	}
	
	public static void doSynchronizationToAnotherServer(Connection connect, List<SynchronizationData> synchronizationDataList) {
		for (SynchronizationData synchronizationData : synchronizationDataList) {
			String dbUser = synchronizationData.getDbUser();
	    	String dbPassword = synchronizationData.getDbPassword();
	    	String dbUrl = synchronizationData.getDbUrl();
	    	String dbDriver = synchronizationData.getDbDriver();
			try {
				// for synchronizing to another server
				Class.forName(dbDriver);
				Connection syncConnect =  DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				syncConnect.setAutoCommit(false);
				Statement syncSt = syncConnect.createStatement();
				// **MUST** insert SqlMarkedReceived first
				syncSt.addBatch(synchronizationData.getSqlMarkedReceived());
				syncSt.addBatch(synchronizationData.getSqlStatement());
				syncSt.executeBatch();
				
				// for updating main database
				Statement st = connect.createStatement();
				String sql = ""
						+ "UPDATE SYNC_INFORMATION "
						+ "SET SQL_SYNC_STATUS_ID = 2, SYNC_TIME = CURRENT_TIMESTAMP "
						+ "WHERE SYNC_INFORMATION_ID = " + synchronizationData.getSyncId();
				st.executeUpdate(sql);
				syncConnect.commit();
				connect.commit();
				if (syncConnect.isClosed()) {
					syncConnect.close();
				}
				
			} catch (Exception e) {
				try {
					Statement st = connect.createStatement();
					String sql = ""
							+ "UPDATE SYNC_INFORMATION "
							+ "SET SQL_SYNC_STATUS_ID = 3 "
							+ "WHERE SYNC_INFORMATION_ID = " + synchronizationData.getSyncId();
					st.executeUpdate(sql);
					connect.commit();
				} catch (SQLException e1) { }
			}
		}
	}
}
