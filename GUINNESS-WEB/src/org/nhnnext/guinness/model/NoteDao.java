package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;


public class NoteDao extends AbstractDao {

	public void createNote(Note note) throws SQLException, ClassNotFoundException {
		String query = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		queryNotForReturn(query, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	@SuppressWarnings("unchecked")
	public List<Note> readNoteList(String groupId) throws ClassNotFoundException, MakingObjectListFromJdbcException, SQLException {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId AND groupId = ? "
				+ "order by targetDate desc limit 10";
		String[] params = { "noteText", "targetDate", "userId", "groupId", "userName" };
		List<?> noteList = queryForReturn(Note.class, params, sql, groupId);
		return (List<Note>) noteList;
	}
}
