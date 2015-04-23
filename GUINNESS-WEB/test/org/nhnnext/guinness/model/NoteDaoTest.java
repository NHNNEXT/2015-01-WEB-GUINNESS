package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.dao.NoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class NoteDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(NoteDaoTest.class);

	@Autowired
	private NoteDao noteDao;
	
	private Note note = new Note("test", "2015-03-19 17:56:24",
			"jyb0823@naver.com", "Ogsho");
	@Test
	public void CreateNote() throws ClassNotFoundException {
		noteDao.createNote(note);
		Note newNote = noteDao.readNote(note.getNoteId());
		assertNotNull(newNote);
	}
	
	@Test
	public void readNoteList() throws Exception {
//		noteDao.readNoteList("ygNDa", endDate, targetDate)
	}
	
	@Test
	public void readExistedNotes() {
		List<Note> noteList = null;
		try {
			noteList = noteDao.readNoteList("Ogsho", "2015-03-21", "2015-03-31", null);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		for (Iterator<Note> i = noteList.iterator(); i.hasNext();) {
			Note note = i.next();
			logger.debug(note.toString());
		}
	}

	@Test
	public void readSingleNote() throws Exception {
		assertNotNull(noteDao.readNote("3"));
	}
	
	@Test
	public void checkGroupNotesCount_노트가없을때() throws Exception {
		assertEquals(0, noteDao.checkGroupNotesCount("oQLZi"));
	}
	
	@Test
	public void checkGroupNotesCount_노트가있을때() throws Exception {
		assertNotEquals(0, noteDao.checkGroupNotesCount("WZDaW"));
	}
}
