CREATE TABLE SYNC_DATABASE_INFORMATION(
DATABASE_INFORMATION_ID SMALLINT NOT NULL,
DATABASE_DESCRIPTION VARCHAR(100),
DB_DRIVER VARCHAR(100),
DB_URL VARCHAR(100),
DB_USER VARCHAR(100),
DB_PASSWORD VARCHAR(100),
IS_ACTIVE SMALLINT,
PRIMARY KEY(DATABASE_INFORMATION_ID)
);

CREATE TABLE SYNC_SQL_SYNC_STATUS(
SQL_SYNC_STATUS_ID SMALLINT NOT NULL,
SQL_SYNC_STATUS_DETAIL VARCHAR(100),
PRIMARY KEY(SQL_SYNC_STATUS_ID)
);

CREATE TABLE SYNC_DESCRIPTION (
EVENT_TABLENAME VARCHAR(100) NOT NULL,
DESCRIPTION VARCHAR(100),
PRIMARY KEY(EVENT_TABLENAME)
);

CREATE TABLE SYNC_MARKED_RECEIVED_DATA(
TABLENAME VARCHAR(100),
ID_OF_TABLE VARCHAR(100)
);

CREATE TABLE SYNC_INFORMATION(
SYNC_INFORMATION_ID INTEGER NOT NULL,
DESCRIPTION VARCHAR(100),
SQL_STATEMENT VARCHAR(500),
SQL_MARKED_RECEIVED VARCHAR(500),
SQL_SYNC_STATUS_ID SMALLINT,
DATABASE_INFORMATION_ID SMALLINT,
CREATE_TIME TIMESTAMP,
SYNC_TIME TIMESTAMP,
PRIMARY KEY(SYNC_INFORMATION_ID),
FOREIGN KEY (SQL_SYNC_STATUS_ID) REFERENCES SYNC_SQL_SYNC_STATUS(SQL_SYNC_STATUS_ID),
FOREIGN KEY (DATABASE_INFORMATION_ID) REFERENCES SYNC_DATABASE_INFORMATION(DATABASE_INFORMATION_ID)
);

CREATE TABLE SYNC_TABLE(
TABLENAME VARCHAR(100) NOT NULL,
PRIMARY KEY(TABLENAME)
);