package org.nhnnext.guinness.service;

import java.util.List;

import javax.annotation.Resource;

import org.nhnnext.guinness.dao.TempNoteDao;
import org.nhnnext.guinness.model.TempNote;
import org.nhnnext.guinness.model.User;
import org.springframework.stereotype.Service;

@Service
public class TempNoteService {
	@Resource
	TempNoteDao tempNoteDao;
	
	public long create(String noteText, String createDate, String sessionUserId) {
		return tempNoteDao.create(new TempNote(noteText, createDate, new User(sessionUserId)));
	}

	public List<TempNote> read(String userId) {
		return tempNoteDao.read(userId);
	}

	public Object readByNoteId(String noteId) {
		return tempNoteDao.readByNoteId(noteId);
	}
}
