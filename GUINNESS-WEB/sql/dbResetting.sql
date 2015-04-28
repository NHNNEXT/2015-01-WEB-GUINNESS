alter table USERS add userStatus char(1) not null default 'Y';

update USERS set userStatus = 'I';

Create Table CONFIRMS(
 keyAddress varchar(100) PRIMARY KEY,
 userId varchar(50),
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(userId) REFERENCES USERS(userId)
);
