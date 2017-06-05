***** README *****
1. วิธีติดตั้งและข้อมูลตารางของระบบ
	1.1 สร้างตารางใน create_table.sql file
	1.2 กำหนดค่าภายในตารางที่สร้างข้างต้น โดยมีรายละเอียดดังนี้
		1.2.1 SYNC_SQL_SYNC_STATUS ==> เป็นการกำหนดค่า status ของข้อมูลที่จะ sync ข้อมูลกันโดย "ต้อง" มีค่าเริ่มต้นดังนี้ (โดยรายละเอียดของ status สามารถเปลี่ยนได้)
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
		1.2.2 SYNC_DESCRIPTION ==> กำหนดคำอธิบายความหมาย โดยกำหนดให้อยู่ในรูปของ EVENT_TABLENAME ตัวอย่าง INSERT_AGS_CLIENT_LANE เป็นต้น โดย INSERT คือ EVENT และ AGS_CLIENT_LANE คือ TABLENAME เมื่อเอามารวมกันจะเป็น INSERT_AGS_CLIENT_LANE
		1.2.3 SYNC_DATABASE_INFORMATION ==> กำหนดข้อมูลของ Database ที่จะทำการ sync ข้อมูล
		1.2.4 SYNC_TABLE ==> กำหนดชื่อตารางที่จะทำการ sync ข้อมูลไปยัง SYNC_DATABASE_INFORMATION ที่กำหนดไว้ **  ปล. ตารางที่จะทำการ sync จะต้องมีการสร้างไว้แล้วทุกที่ที่มีการเชื่อมต่อกัน **
		1.2.5 SYNC_MARKED_RECEIVED_DATA ==> เป็นตาราง tmp ไว้สำหรับใช้สำหรับ software เท่านั้น
		1.2.6 SYNC_INFORMATION ==> เป็นตารางข้อมูลในการ sync ของแต่ละ Database ไม่มีการ sync ข้อมูลในตารางนี้ไปยัง Database อื่น

2. ข้อจำกัด
	2.1 การกำหนดข้อมูลในหัวข้อ 1.2.1-1.2.4 จะต้องกำหนดข้อมูลเข้าไปเอง และการกำหนดมีผลต่อการทำงานของ software
	2.2 ในระบบจะค้นหา primary key เพื่อหา MAIN_ID ของตาราง (จะค้นหาเฉพาะตารางที่มีการกำหนด ชื่อตาราง ใน SYNC_TABLE เท่านั้น) หากระบบทำการค้นหา primary key ไม่เจอหรือมี primary key มากกว่า 1 fields ระบบจะใช้ MAIN_ID ในรูปแบบนี้ ==> TABLENAME_ID 
		เช่น AGS_CLIENT_LANE_ID โดย AGS_CLIENT_LANE เป็นชื่อตาราง(TABLENAME) เมื่อเอามารวมกันจะเป็น AGS_CLIENT_LANE_ID