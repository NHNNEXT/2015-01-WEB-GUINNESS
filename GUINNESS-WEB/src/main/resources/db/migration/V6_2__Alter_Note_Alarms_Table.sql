ALTER TABLE NOTE_ALARMS ADD commentId bigint(20);
ALTER TABLE NOTE_ALARMS MODIFY commentId bigint(20) after noteId;
SET foreign_key_checks = 0;
ALTER TABLE NOTE_ALARMS ADD FOREIGN KEY (commentId) REFERENCES COMMENTS(commentId) ON DELETE CASCADE;
SET foreign_key_checks = 1;
