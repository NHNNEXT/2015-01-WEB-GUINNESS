package org.nhnnext.guinness.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.model.Note;
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
	
	@Test
	public void CreateNote() throws ClassNotFoundException {
		Note note = new Note("test", "2015-03-19 17:56:24", "jyb0823@naver.com", "Ogsho");
		noteDao.createNote(note);
		Note newNote = noteDao.readNote(note.getNoteId());
		assertNotNull(newNote);
	}
	
	@Test
	public void readExistedNotes() {
		List<Note> noteList = null;
		try {
			noteList = noteDao.readNoteList("Ogsho", "2015-03-21", "2015-03-31", "test@naver.com");
			for (Iterator<Note> i = noteList.iterator(); i.hasNext();) {
				Note note = i.next();
				logger.debug(note.toString());
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
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
	
	@Test
	public void deleteNote_권한이있을때() throws Exception {
		
		String noteId = "21";
		
		assertEquals(1, noteDao.deleteNote(noteId));
	}
	
	@Test
	public void update() throws Exception {
		String noteId = "3";
		String text = "수정된 내용";
		noteDao.updateNote(text, noteId);
		assertEquals(text, noteDao.readNote(noteId).getNoteText());
	}
}
