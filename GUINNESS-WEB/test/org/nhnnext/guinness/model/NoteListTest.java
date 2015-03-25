package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoteListTest {

	@Test
	public void findBygroupId() {
		
		NoteDao noteDAO = new NoteDao();
		
		NoteList noteList = noteDAO.findByGroupId("RAZc11I","2015-03-11 %");
		assertNull(noteList);
		
		noteList = noteDAO.findByGroupId("RAZcI","2015-03-11 %");
		assertNotNull(noteList);
	}

}
