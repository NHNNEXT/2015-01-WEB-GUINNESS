alter table PREVIEWS drop foreign key previews_ibfk_1;
alter table PREVIEWS drop foreign key previews_ibfk_2;

alter table PREVIEWS add constraint foreign key(noteId) references NOTES(noteId) ON DELETE CASCADE;
alter table PREVIEWS add constraint foreign key(groupId) references GROUPS(groupId) ON DELETE CASCADE;

