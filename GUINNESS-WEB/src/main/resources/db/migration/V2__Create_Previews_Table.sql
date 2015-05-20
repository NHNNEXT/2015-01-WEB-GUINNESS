create table PREVIEWS(
 noteId bigint,
 groupId char(5),
 attentionText text,
 questionText text,
 createDate DATETIME DEFAULT CURRENT_TIMESTAMP,
 Foreign Key(noteId) REFERENCES NOTES(noteId),
 Foreign Key(groupId) REFERENCES GROUPS(groupId),
 Primary Key(noteId, groupId)
 );