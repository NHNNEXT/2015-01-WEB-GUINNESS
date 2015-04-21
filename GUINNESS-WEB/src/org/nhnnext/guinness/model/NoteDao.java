package org.nhnnext.guinness.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class NoteDao extends JdbcDaoSupport {
	public void createNote(Note note) {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		
		getJdbcTemplate().update(sql, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	public List<Note> readNoteList(String groupId, String endDate, String targetDate) {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId and groupId = ? and NOTES.targetDate between ? and ? order by targetDate desc";
		
		try {
			return getJdbcTemplate().query(sql, (rs, rowNum) -> new Note(
					rs.getString("noteId"), rs.getString("noteText"),
					rs.getString("targetDate"), rs.getString("userId"), 
					rs.getString("groupId"), rs.getString("userName")), groupId, endDate, targetDate);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Note>();
		}
	}

	public int checkGroupNotesCount(String groupId) {
		String sql = "select count(*) from NOTES where groupId=?";
		
		return getJdbcTemplate().queryForObject(sql, Integer.class, groupId);
	}

	public Note readNote(String noteId) {
		String sql = "select *from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		
		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Note(
					rs.getString("noteId"), rs.getString("noteText"),
					rs.getString("targetDate"), rs.getString("userId"), 
					rs.getString("groupId"), rs.getString("userName")
					), noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
