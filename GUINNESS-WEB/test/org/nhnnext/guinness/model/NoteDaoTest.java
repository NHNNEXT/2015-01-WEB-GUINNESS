package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class NoteDaoTest {
	NoteDao noteDAO = new NoteDao();

	@Test
	public void testGetConnection() {
		Connection conn = noteDAO.getConnection();
		
		assertNotNull(conn);
	}

	@Test
	public void testCreateNote() throws SQLException {
		Note note = new Note("test", "2015-03-19 17:56:24", "test@guinness.org", "abcde");
		
		noteDAO.createNote(note);
	}
}
