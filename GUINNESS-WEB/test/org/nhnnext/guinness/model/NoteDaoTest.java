package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.nhnnext.guinness.controller.notes.ReadNoteListController;
import org.nhnnext.guinness.model.dao.NoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListController.class);
	NoteDao noteDao = NoteDao.getInstance();

	@Test
	public void CreateNote() throws ClassNotFoundException {
		Note note = new Note("test", "2015-03-19 17:56:24", "jyb0823@naver.com", "Ogsho");

		noteDao.createNote(note);
	}

	@Test
	public void readNotes() {
		List<Note> noteList = null;
		try {
			noteList = noteDao.readNoteList("Ogsho", "2015-03-21", "2015-03-31");
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		for (Iterator<Note> i = noteList.iterator(); i.hasNext();) {
			Note note = i.next();
			logger.debug(note.toString());
		}
	}

	@Test
	public void testName() throws Exception {
		NoteDao noteDao = NoteDao.getInstance();
		int tt = noteDao.checkGroupNotesCount("JFaTh");
		logger.debug(Integer.toString(tt));
	}

	public void readSingleNote() throws Exception {
		Note note = null;

		note = noteDao.readNote("3");

		assertNotNull(note);
	}
}
