drop database GUINNESS;

drop user 'link413'@'localhost';

/* DB 생성 */

Create DATABASE GUINNESS DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

/* User 생성 및 DB권한 주기 */
Create User link413@'localhost' identified by 'link413';

Grant all privileges on GUINNESS.* to link413@'localhost' identified by 'link413';

/* Table 생성 */

use GUINNESS;

Create Table USERS(
 userId varchar(50) PRIMARY KEY,
 userName varchar(50) Not Null,
 userPassword varchar(16) Not Null,
 userStatus char(1) DEFAULT 'R',
 userImage varchar(54) DEFAULT "avatar-default.png",
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP
);

Create Table GROUPS(
 groupId char(5) PRIMARY KEY,
 groupName varchar(50) Not Null,
 groupCaptainUserId varchar(50),
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 isPublic char(1) default 'F',
 Foreign Key(groupCaptainUserId) REFERENCES USERS(userId)
);

Create Table GROUPS_USERS(
 userId varchar(50),
 groupId char(5),
 Foreign Key(userId) REFERENCES USERS(userId),
 Foreign Key(groupId) REFERENCES GROUPS(groupId) on delete cascade,
 Primary Key(userId, groupId)
);

Create Table NOTES(
 noteId bigint PRIMARY KEY AUTO_INCREMENT,
 noteText text Not Null,
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 targetDate DATETIME Not Null,
 userId varchar(50),
 groupId char(5),
 commentCount int,
 Foreign Key(userId) REFERENCES USERS(userId),
 Foreign Key(groupId) REFERENCES GROUPS(groupId) on delete cascade
);

Create Table COMMENTS (
  commentId bigint PRIMARY KEY AUTO_INCREMENT,
  commentText text Not Null,
  commentType char(1) Not Null,
  createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  userId varchar(50),
  noteId bigint,
  Foreign Key(userId) REFERENCES USERS(userId)  on delete cascade,
  Foreign Key(noteId) REFERENCES NOTES(noteId)  on delete cascade
);

Create Table CONFIRMS(
 keyAddress varchar(100) PRIMARY KEY,
 userId varchar(50),
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(userId) REFERENCES USERS(userId)
);

/* TEST USER SET */

insert into USERS values('g@g.g', '기얏토', '1234qwer','E' , default , default);
insert into USERS values('a@a.a', '알파', '1234qwer' ,'E' , default , default);
insert into USERS values('h@h.h', '휘바', '1234qwer' ,'E' , default , default);
insert into USERS values('m@m.m', '모카', '1234qwer' ,'E' , default , default);
insert into USERS values('d@d.d', '다스', '1234qwer' ,'E' , default , default);
insert into USERS values('y@y.y', '와이빈', '1234qwer' ,'E' , default , default);
insert into USERS values('j@j.j', '승미', '1234qwer' ,'E' , default , default);
insert into USERS values('s@s.s', '송희', '1234qwer' ,'E' , default , default);

alter table USERS modify userImage varchar(50) default "avatar-default.png";

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


/* 소나큐브용 데이터베이스 생성 및 설정. */
mysql -u root -p
CREATE DATABASE sonar DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
create user sonar identified by 'link413';
GRANT ALL PRIVILEGES on  sonar.*  to 'sonar'@'localhost' identified BY 'sonar' WITH GRANT OPTION;
flush privileges;