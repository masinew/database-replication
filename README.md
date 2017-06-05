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
		  
		    
# วิธีติดตั้งและข้อมูลตารางของระบบ 
	1. สร้างตารางใน create_table.sql file (PATH: [project_directory/sql/DB2/create_table.sql])  
	2. กำหนดค่าภายในตารางที่สร้างข้างต้น โดยมีรายละเอียดดังนี้  
		2.1 SYNC_SQL_SYNC_STATUS ==> เป็นการกำหนดค่า status ของข้อมูลที่จะ sync ข้อมูลกันโดย "ต้อง" มีค่าเริ่มต้นดังนี้ (โดยรายละเอียดของ status สามารถเปลี่ยนได้)  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				1,  
				'ยังไม่ได้ดำเนินการ'  
			);  
  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				2,  
				'ดำเนินการแล้ว'  
			);  
  
			INSERT INTO SYNC_SQL_SYNC_STATUS VALUES(  
				3,  
				'ดำเนินการไม่สำเร็จ'  
			);  
		2.2 SYNC_DESCRIPTION ==> กำหนดคำอธิบายความหมาย โดยกำหนดให้อยู่ในรูปของ EVENT_TABLENAME ตัวอย่าง INSERT_AGS_CLIENT_LANE เป็นต้น โดย INSERT คือ EVENT และ AGS_CLIENT_LANE คือ TABLENAME เมื่อเอามารวมกันจะเป็น INSERT_AGS_CLIENT_LANE  
		2.3 SYNC_DATABASE_INFORMATION ==> กำหนดข้อมูลของ Database ที่จะทำการ sync ข้อมูล  
		2.4 SYNC_TABLE ==> กำหนดชื่อตารางที่จะทำการ sync ข้อมูลไปยัง SYNC_DATABASE_INFORMATION ที่กำหนดไว้ **  ปล. ตารางที่จะทำการ sync จะต้องมีการสร้างไว้แล้วทุกที่ที่มีการเชื่อมต่อกัน **  
		2.5 SYNC_MARKED_RECEIVED_DATA ==> เป็นตาราง tmp ไว้สำหรับใช้สำหรับ software เท่านั้น  
		2.6 SYNC_INFORMATION ==> เป็นตารางข้อมูลในการ sync ของแต่ละ Database ไม่มีการ sync ข้อมูลในตารางนี้ไปยัง Database อื่น  
  
# ข้อจำกัด  
	1. การกำหนดข้อมูลในหัวข้อ 1.2.1-1.2.4 จะต้องกำหนดข้อมูลเข้าไปเอง และการกำหนดมีผลต่อการทำงานของ software  
	2 ในระบบจะค้นหา primary key เพื่อหา MAIN_ID ของตาราง (จะค้นหาเฉพาะตารางที่มีการกำหนด ชื่อตาราง ใน SYNC_TABLE เท่านั้น) หากระบบทำการค้นหา primary key ไม่เจอหรือมี primary key มากกว่า 1 fields ระบบจะใช้ MAIN_ID ในรูปแบบนี้ ==> TABLENAME_ID  
		เช่น AGS_CLIENT_LANE_ID โดย AGS_CLIENT_LANE เป็นชื่อตาราง(TABLENAME) เมื่อเอามารวมกันจะเป็น AGS_CLIENT_LANE_ID  
