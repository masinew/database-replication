# database-replication

# Installation and System table information
	1. Create system table (Look at: [PROJECT_DIRECTORY]/sql/DB2/create_table.sql).  
	2. System table information.  
		2.1 SYNC_SQL_SYNC_STATUS => make your "sync status" on this table and **MUST** have this status(You can change a description on each row):  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				1,  
				'DO NOT DO ANYTHING'  
			);  
  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				2,  
				'SUCCESS'  
			);  
  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				3,  
				'UNSUCCESS'  
			);  
		2.2 SYNC_DESCRIPTION => Define description for any event by using this format: EVENT_TABLENAME. eg. Your table name is MASINEW and your event for describing is INSERT. Combined it together look like this: INSERT_MASINEW.  
		2.3 SYNC_DATABASE_INFORMATION => Define your database information in this table. eg. DB_DRIVER, DB_URL, DB_USER and DB_PASSWORD.  
		2.4 SYNC_TABLE => Define your table to sync data to another database.  
		2.5 SYNC_MARKED_RECEIVED_DATA => TMP table for this system.  
		2.6 SYNC_INFORMATION => Synchronization information for explaning to you that what happened in this database and this table do not sync information to another database.  

# Limitation
	1. Defination on 2.1-2.4 in Installation and System table information section is important to use by this Software. So you have to be careful inserting information.  
	2. Software wile search a primary key on tables(tables are only defined in SYNC_TABLE). So if software didn't find primary key or the table have primary key more than one. Default ID for the table is TABLENAME_ID format.  
		eg. Your table name is MASINEW. So your id for this table is MASINEW_ID.  
		PS.> This ID is for creating trigger in this system for each table and each events(INSERT, UPDATE and DELETE)  
