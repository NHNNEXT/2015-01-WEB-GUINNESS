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

	public Object readByNoteId(long noteId) {
		return tempNoteDao.readByNoteId(noteId);
	}

	public void delete(String noteId) {
		tempNoteDao.delete(noteId);
	}

	public boolean update(long noteId, String noteText, String createDate) {
		if(tempNoteDao.update(new TempNote(noteId, noteText, createDate)) == 1) {
			return true;
		}
		return false;
	}
}
