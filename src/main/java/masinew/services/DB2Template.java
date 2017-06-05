package masinew.services;

import java.util.List;

class DB2Template {
	public static String getInsertAfterTriggerTemplate(String tableName, String tableFieldId, List<String> fieldList) {
		String fieldSet = "";
		String fieldVal = "";
		for (int i=0; i<fieldList.size(); i++) {
			String fieldName = fieldList.get(i);
			if (i == 0) {
				fieldSet += fieldName;
				fieldVal += "CONCAT NEW." + fieldName + " ";
				continue;
			}
			
			fieldSet += ", " + fieldName;
			fieldVal += "CONCAT ''', ''' " + "CONCAT NEW." + fieldName + " ";
			
		}
		
		String template = "CREATE OR REPLACE TRIGGER SYNC_" + tableName + "_AFTER_INSERT_TRIGGER " +
			     "AFTER INSERT ON " + tableName + " " +
			     "REFERENCING NEW AS NEW " +
			     "FOR EACH ROW " +
			     "BEGIN ATOMIC " +
			     	"DECLARE DESCRIPTION VARCHAR(100); "+
			     	"DECLARE SQL_STATEMENT VARCHAR(500); " +
			     	"DECLARE SQL_MARKED_RECEIVED VARCHAR(500); " +
			     	"DECLARE IS_CONTINUE SMALLINT; " +
			     	"SET IS_CONTINUE = (SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE 0 END AS IS_CONTINUE FROM SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = NEW." + tableFieldId +"); " +
			     	"IF IS_CONTINUE = 1 THEN " +
			     		"SET DESCRIPTION = (SELECT DESCRIPTION FROM SYNC_DESCRIPTION WHERE EVENT_TABLENAME = 'INSERT_" + tableName + "'); "+
				     	"SET SQL_STATEMENT = ( " +
				     		"'INSERT INTO " + tableName + "(" + fieldSet + ") VALUES " +
				     		"(''' " + 
				     		fieldVal + 
				 			"CONCAT ''')' " +
				 		"); " +
				 		"SET SQL_MARKED_RECEIVED = ( " +
				 			"'INSERT INTO SYNC_MARKED_RECEIVED_DATA(TABLENAME, ID_OF_TABLE) VALUES(''' " +
				 			"CONCAT '" + tableName + "' " +
				 			"CONCAT ''',''' " +
				 			"CONCAT NEW." + tableFieldId + " " +
				 			"CONCAT ''')' " +
				 		"); " +
				     	
				     	"CALL INSERT_SYNC_INFORMATION(DESCRIPTION, SQL_MARKED_RECEIVED, SQL_STATEMENT); " +
				    "ELSE " +
				    	"DELETE SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = NEW." + tableFieldId + "; " +
			     	"END IF; " +
			     "END ";
		
		return template;
	}
	
	public static String getUpdateAfterTriggerTemplate(String tableName, String tableFieldId, List<String> fieldList) {
		String template = "CREATE OR REPLACE TRIGGER SYNC_" + tableName + "_AFTER_UPDATE_TRIGGER " +
			     "AFTER UPDATE ON " + tableName + " " +
			     "REFERENCING NEW AS NEW " +
			     "FOR EACH ROW " +
			     "BEGIN ATOMIC " +
			     	"DECLARE DESCRIPTION VARCHAR(100); "+
			     	"DECLARE SQL_STATEMENT VARCHAR(500); " +
			     	"DECLARE SQL_MARKED_RECEIVED VARCHAR(500); " +
			     	"DECLARE IS_CONTINUE SMALLINT; " +
			     	"SET IS_CONTINUE = (SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE 0 END AS IS_CONTINUE FROM SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = NEW." + tableFieldId +"); " +
			     	"IF IS_CONTINUE = 1 THEN " +
			     		"SET DESCRIPTION = (SELECT DESCRIPTION FROM SYNC_DESCRIPTION WHERE EVENT_TABLENAME = 'UPDATE_" + tableName + "'); "+
				     	"SET SQL_STATEMENT = ( " +
				     		"'UPDATE " + tableName + " SET ";
		for(int i=0; i<fieldList.size(); i++) {
			String fieldName = fieldList.get(i);
			if (i == 0) {
				template += fieldName + " = ''' CONCAT NEW." + fieldName + " CONCAT ''' ";
				continue;
			}
			
			template += ", " + fieldName + " = ''' CONCAT NEW." + fieldName + " CONCAT ''' ";
		}
		
		template += "WHERE " + tableFieldId + " = ''' CONCAT NEW." + tableFieldId + " CONCAT '''' );";
		
		template += "SET SQL_MARKED_RECEIVED = ( " +
				 			"'INSERT INTO SYNC_MARKED_RECEIVED_DATA(TABLENAME, ID_OF_TABLE) VALUES(''' " +
				 			"CONCAT '" + tableName + "' " +
				 			"CONCAT ''',''' " +
				 			"CONCAT NEW." + tableFieldId + " " +
				 			"CONCAT ''')' " +
				 		"); " +
				     	
				     	"CALL INSERT_SYNC_INFORMATION(DESCRIPTION, SQL_MARKED_RECEIVED, SQL_STATEMENT); " +
				    "ELSE " +
				    	"DELETE SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = NEW." + tableFieldId + "; " +
			     	"END IF; " +
			     "END ";
		
		return template;
	}
	
	public static String getDeleteAfterTriggerTemplate(String tableName, String tableFieldId, List<String> fieldList) {
		String template = "CREATE OR REPLACE TRIGGER SYNC_" + tableName + "_AFTER_DELETE_TRIGGER " +
			     "AFTER DELETE ON " + tableName + " " +
			     "REFERENCING OLD AS OLD " +
			     "FOR EACH ROW " +
			     "BEGIN ATOMIC " +
			     	"DECLARE DESCRIPTION VARCHAR(100); "+
			     	"DECLARE SQL_STATEMENT VARCHAR(500); " +
			     	"DECLARE SQL_MARKED_RECEIVED VARCHAR(500); " +
			     	"DECLARE IS_CONTINUE SMALLINT; " +
			     	"SET IS_CONTINUE = (SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE 0 END AS IS_CONTINUE FROM SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = OLD." + tableFieldId +"); " +
			     	"IF IS_CONTINUE = 1 THEN " +
			     		"SET DESCRIPTION = (SELECT DESCRIPTION FROM SYNC_DESCRIPTION WHERE EVENT_TABLENAME = 'DELETE_" + tableName + "'); "+
				     	"SET SQL_STATEMENT = ( " +
				     		"'DELETE " + tableName + " WHERE " + tableFieldId + " = ''' CONCAT OLD. " + tableFieldId + " CONCAT '''' ); " +
				 		"SET SQL_MARKED_RECEIVED = ( " +
				 			"'INSERT INTO SYNC_MARKED_RECEIVED_DATA(TABLENAME, ID_OF_TABLE) VALUES(''' " +
				 			"CONCAT '" + tableName + "' " +
				 			"CONCAT ''',''' " +
				 			"CONCAT OLD." + tableFieldId + " " +
				 			"CONCAT ''')' " +
				 		"); " +
				     	
				     	"CALL INSERT_SYNC_INFORMATION(DESCRIPTION, SQL_MARKED_RECEIVED, SQL_STATEMENT); " +
				    "ELSE " +
				    	"DELETE SYNC_MARKED_RECEIVED_DATA WHERE TABLENAME = '" + tableName + "' AND ID_OF_TABLE = OLD." + tableFieldId + "; " +
			     	"END IF; " +
			     "END ";
		
		return template;
	}
}
