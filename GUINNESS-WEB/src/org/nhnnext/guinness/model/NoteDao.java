package org.nhnnext.guinness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class NoteDao extends JdbcDaoSupport {

	public void createNote(Note note) {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		getJdbcTemplate().update(sql, note.getNoteText(), note.getTargetDate(),
				note.getUserId(), note.getGroupId());
	}

	public List<Note> readNoteList(String groupId, String endDate,
			String targetDate) {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId and groupId = ? and NOTES.targetDate between ? and ? order by targetDate desc";
		RowMapper<Note> rowMapper = new RowMapper<Note>() {
			@Override
			public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Note(rs.getString("noteId"),
						rs.getString("noteText"), rs.getString("targetDate"),
						rs.getString("userId"), rs.getString("groupId"),
						rs.getString("userName"));
			}
		};
		try {
			return getJdbcTemplate().query(sql, rowMapper, groupId, endDate,
					targetDate);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int checkGroupNotesCount(String groupId) {
		String sql = "select count(*) from NOTES where groupId=?";
		return getJdbcTemplate().queryForObject(sql, new Object[] { 10 }, Integer.class);
	}

	public Note readNote(String noteId) {
		String sql = "select * from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		RowMapper<Note> rowMapper = new RowMapper<Note>() {
			@Override
			public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Note(rs.getString("noteId"),
						rs.getString("noteText"), rs.getString("targetDate"),
						rs.getString("userId"), rs.getString("groupId"),
						rs.getString("userName"));
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rowMapper, noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
