alter table USERS add userStatus char(1) not null default 'R';

update USERS set userStatus = 'E';

Create Table CONFIRMS(
 keyAddress varchar(100) PRIMARY KEY,
 userId varchar(50),
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(userId) REFERENCES USERS(userId)
);

insert into USERS values('g@g.g', '기얏토', '1234qwer', default , default, 'E');
insert into USERS values('a@a.a', '알파', '1234qwer' , default , default, 'E');
insert into USERS values('h@h.h', '휘바', '1234qwer' , default , default, 'E');
insert into USERS values('m@m.m', '모카', '1234qwer' , default , default, 'E');
insert into USERS values('d@d.d', '다스', '1234qwer' , default , default, 'E');
insert into USERS values('y@y.y', '와이빈', '1234qwer' , default , default, 'E');