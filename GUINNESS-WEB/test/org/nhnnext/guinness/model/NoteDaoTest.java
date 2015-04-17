package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
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
	public void readExistedNotes() {
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
		int tt = noteDao.checkGroupNotesCount("JFaTh");
		logger.debug(Integer.toString(tt));
	}

	public void readSingleNote() throws Exception {
		Note note = null;
		note = noteDao.readNote("3");
		assertNotNull(note);
	}
}
