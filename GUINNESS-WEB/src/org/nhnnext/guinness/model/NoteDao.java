package org.nhnnext.guinness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.util.AbstractDao;
import org.nhnnext.guinness.util.ObjectMapper;

public class NoteDao extends AbstractDao {
	private static NoteDao noteDao = new NoteDao();

	private NoteDao() {

	}

	public static NoteDao getInstance() {
		return noteDao;
	}

	public void createNote(Note note) throws ClassNotFoundException {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		queryNotForReturn(sql, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	@SuppressWarnings("unchecked")
	public List<Note> readNoteList(String groupId, String endDate, String targetDate)
			throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId and groupId = ? and NOTES.targetDate between ? and ? order by targetDate desc";
		ObjectMapper<Note> om = new ObjectMapper<Note>() {
			@Override
			public Note returnObject(ResultSet rs) throws SQLException {
				return new Note(rs.getString("noteId"), rs.getString("noteText"), rs.getString("targetDate"),
						rs.getString("userId"), rs.getString("groupId"), rs.getString("userName"));
			}
		};
		List<?> noteList = queryForObjectsReturn(om, sql, groupId, endDate, targetDate);
		return (List<Note>) noteList;
	}

	public int checkGroupNotesCount(String groupId) throws ClassNotFoundException {
		String sql = "select * from NOTES where groupId=?";
		return queryForCountReturn(sql, groupId);
	}

	public Note readNote(String noteId) throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String sql = "select *from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		ObjectMapper<Note> om = new ObjectMapper<Note>() {
			@Override
			public Note returnObject(ResultSet rs) throws SQLException {
				return new Note(rs.getString("noteId"), rs.getString("noteText"), rs.getString("targetDate"),
						rs.getString("userId"), rs.getString("groupId"), rs.getString("userName"));
			}
		};
		List<?> note = queryForObjectsReturn(om, sql, noteId);
		return (Note) note.get(0);
	}
}
