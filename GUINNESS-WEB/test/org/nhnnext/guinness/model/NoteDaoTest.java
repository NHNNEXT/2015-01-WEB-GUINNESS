package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class NoteDaoTest {
	NoteDao noteDao = new NoteDao();

	@Test
	public void CreateNote() throws SQLException, ClassNotFoundException {
		Note note = new Note("test", "2015-03-19 17:56:24", "jyb0823@naver.com", "Ogsho");
		
		noteDao.createNote(note);
	}
	
	@Test
	public void readNotes() {
		List<Note> noteList = null;
		try {
			noteList = noteDao.readNoteList("Ogsho", "2015-03-21", "2015-03-31");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		for(Iterator<Note> i =  noteList.iterator(); i.hasNext(); ) {
			Note note = i.next();
			System.out.println(note.toString());
		}
	}
	
	@Test

	public void testName() throws Exception {
		NoteDao noteDao = new NoteDao();
		int tt = noteDao.checkGroupNotesCount("JFaTh");
		System.out.println(tt);
	}
	
	
    public void readSingleNote() throws Exception {
	    Note note = null;
	    
	    note = noteDao.readNote("3");
	    
	    assertNotNull(note);
    }
}
