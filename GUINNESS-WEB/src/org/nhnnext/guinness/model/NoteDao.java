package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.util.AbstractDao;

public class NoteDao extends AbstractDao {
	private static NoteDao noteDao = new NoteDao();
	
	private NoteDao() {
		
	}
	
	public static NoteDao getInstance() {
		return noteDao;
	}
	public void createNote(Note note) throws SQLException, ClassNotFoundException {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		queryNotForReturn(sql, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	@SuppressWarnings("unchecked")
	public List<Note> readNoteList(String groupId, String endDate, String targetDate) throws MakingObjectListFromJdbcException, SQLException, ClassNotFoundException {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId and groupId = ? and NOTES.targetDate between ? and ? order by targetDate desc";
		String[] params = { "noteId", "noteText", "targetDate", "userId", "groupId", "userName" };
		List<?> noteList = queryForObjectsReturn(Note.class, params, sql, groupId, endDate, targetDate);
		return (List<Note>) noteList;
	}
	
	public int checkGroupNotesCount(String groupId) throws SQLException, ClassNotFoundException {
		String sql = "select * from NOTES where groupId=?";
		return queryForCountReturn(sql, groupId); 
	}
	
	public Note readNote(String noteId) throws MakingObjectListFromJdbcException, SQLException, ClassNotFoundException {
		String sql = "select *from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		String[] params = { "noteId", "noteText", "targetDate", "userId", "groupId", "userName" };
		List<?> note = queryForObjectsReturn(Note.class, params, sql, noteId);
		return (Note) note.get(0);
    }
}
