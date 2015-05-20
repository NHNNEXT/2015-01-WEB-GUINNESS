Create Table GROUP_ALARMS(
 alarmId varchar(10) NOT NULL,
 calleeId varchar(50) NOT NULL,
 callerId varchar(50) NOT NULL,
 groupId char(5) NOT NULL,
 alarmStatus char(1) NOT NULL,
 alarmCreateDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(calleeId) REFERENCES USERS(userId) on delete cascade,
 Foreign Key(callerId) REFERENCES USERS(userId) on delete cascade,
 Foreign Key(groupId) REFERENCES GROUPS(groupId) on delete cascade
);

ALTER TABLE ALARMS RENAME NOTE_ALARMS;
