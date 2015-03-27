package org.nhnnext.guinness.model;

import java.lang.reflect.InvocationTargetException;
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
			noteList = noteDao.readNoteList("Ogsho");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		for(Iterator<Note> i =  noteList.iterator(); i.hasNext(); ) {
			Note note = i.next();
			System.out.println(note.toString());
		}
	}
}
