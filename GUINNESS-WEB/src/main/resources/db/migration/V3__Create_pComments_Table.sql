Create Table PCOMMENTS (
  pCommentId bigint PRIMARY KEY AUTO_INCREMENT,
  pId int NOT NULL,
  userId varchar(50),
  noteId bigint,
  sameSenCount int DEFAULT 0 NOT NULL,
  sameSenIndex int DEFAULT 0 NOT NULL,
  pCommentText text NOT NULL,
  selectedText text NOT NULL,
  pCommentCreateDate DATETIME DEFAULT CURRENT_TIMESTAMP,
  Foreign Key(userId) REFERENCES USERS(userId)  on delete cascade,
  Foreign Key(noteId) REFERENCES NOTES(noteId)  on delete cascade
);

