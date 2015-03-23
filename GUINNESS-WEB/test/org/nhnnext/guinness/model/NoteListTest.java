package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoteListTest {

	@Test
	public void findBygroupId() {
		
		NoteDAO noteDAO = new NoteDAO();
		
		NoteList noteList = noteDAO.findByGroupId("abcde");
		
		System.out.println(noteList.getItems().size());
		for (int i = 0; i < noteList.getItems().size(); i++) {
			System.out.println(noteList.getItems().get(i).getNoteText());
		}
		assertNotNull(noteList);
	}

}
