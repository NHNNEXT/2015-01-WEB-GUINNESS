Create Table ALARMS(
 alarmId varchar(10) NOT NULL,
 calleeId varchar(50) NOT NULL,
 callerId varchar(50) NOT NULL,
 noteId bigint NOT NULL,
 alarmText text NOT NULL,
 alarmStatus char(1) NOT NULL,
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(calleeId) REFERENCES USERS(userId) on delete cascade,
 Foreign Key(callerId) REFERENCES USERS(userId) on delete cascade,
 Foreign Key(noteId) REFERENCES NOTES(noteId) on delete cascade
);

