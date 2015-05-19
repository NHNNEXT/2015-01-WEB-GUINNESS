ALTER TABLE ALARMS ADD invitedGroupId char(5) NOT NULL;
ALTER TABLE ALARMS MODIFY invitedGroupId char(5) NOT NULL after noteId;
SET foreign_key_checks = 0;
ALTER TABLE ALARMS ADD FOREIGN KEY (invitedGroupId) REFERENCES GROUPS(groupId) ON DELETE CASCADE;
SET foreign_key_checks = 1;

/* 그룹초대 알림을 위한 가상노트, 가상코멘트 */
insert into GROUPS (groupId) values('-1');
insert into NOTES (noteId, groupId) values('-1', '-1');
insert into COMMENTS (commentId) values('-1');
