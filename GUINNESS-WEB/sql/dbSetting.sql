drop database GUINNESS;

drop user 'link413'@'localhost';

/* DB 생성 */

Create DATABASE GUINNESS;

/* User 생성 및 DB권한 주기 */
Create User link413@'localhost' identified by 'link413';

Grant all privileges on GUINNESS.* to link413@'localhost' identified by 'link413';

/* Table 생성 */

use GUINNESS;

Create Table USERS(
 userId varchar(50) PRIMARY KEY,
 userName varchar(50) Not Null,
 userPassword varchar(16) Not Null,
 userImage BLOB,
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
 Foreign Key(userId) REFERENCES USERS(userId),
 Foreign Key(groupId) REFERENCES GROUPS(groupId) on delete cascade
);

Create Table COMMENTS (
  commentId bigint PRIMARY KEY AUTO_INCREMENT,
  comentText text Not Null,
  comentType char(1) Not Null,
  createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  userId varchar(50),
  noteId bigint,
  Foreign Key(userId) REFERENCES USERS(userId),
  Foreign Key(noteId) REFERENCES NOTES(noteId)
);
