package org.nhnnext.guinness.dao;

import java.util.ArrayList;
import java.util.List;

import org.nhnnext.guinness.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class NoteDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(NoteDao.class);

	public void createNote(Note note) {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId, commentCount) values(?, ?, ?, ?, 0)";

		getJdbcTemplate().update(sql, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	public List<Note> readNoteList(String groupId, String endDate, String targetDate, String userIds) {
		String sql = "select * from NOTES, USERS " + "where NOTES.userId = USERS.userId " + "and groupId = ? "
				+ "and NOTES.targetDate between ? and ? ";
		if (userIds != null) {
			sql += "and NOTES.userId in (" + userIds + ") ";
		}
		sql += "order by targetDate desc";

		try {
			return getJdbcTemplate().query(
					sql,
					(rs, rowNum) -> new Note(rs.getString("noteId"), rs.getString("noteText"), rs
							.getString("targetDate"), rs.getString("userId"), rs.getString("groupId"), rs
							.getString("userName"), rs.getInt("commentCount"), rs.getString("userImage")), groupId,
					endDate, targetDate);
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
			return getJdbcTemplate().queryForObject(
					sql,
					(rs, rowNum) -> new Note(rs.getString("noteId"), rs.getString("noteText"), rs
							.getString("targetDate"), rs.getString("userId"), rs.getString("groupId"), rs
							.getString("userName"), rs.getInt("commentCount"), rs.getString("userImage")), noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void increaseCommentCount(String noteId) {
		String sql = "update NOTES set commentCount = commentCount + 1 where noteId = ?";
		getJdbcTemplate().update(sql, noteId);
	}

	public void decreaseCommentCount(String commentId) {
		String sql = "UPDATE NOTES SET commentCount = commentCount -1 where noteId = (select noteId from COMMENTS where commentId = ?)";
		getJdbcTemplate().update(sql, commentId);
	}

	public int deleteNote(String noteId) {
		String sql = "delete from NOTES where noteId = ?";
		logger.debug(" noteId : {} ", noteId);
		return getJdbcTemplate().update(sql, noteId);
	}

	public void updateNote(String text, String noteId) {
		String sql = "UPDATE NOTES SET noteText = ? where noteId = ?";
		getJdbcTemplate().update(sql, text, noteId);
	}

	public List<Note> searchQuery(String userId, String... words) {
		String query = "";
		for (String word : words) {
			query+=" OR N.noteText like \"%"+word+"%\"";
		}
		String sql = "SELECT distinct noteId, noteText, targetDate, N.userId, N.groupId, U.userName, G.groupName, N.commentCount FROM NOTES N LEFT JOIN USERS U ON N.userId = U.userId LEFT JOIN GROUPS G ON N.groupId = G.groupId LEFT JOIN GROUPS_USERS GU on GU.groupId = N.groupId WHERE "
				+ query.substring(3)
				+ " and N.groupId in (select groupId from GROUPS_USERS where userId = ?) AND N.userId = GU.userId order by N.targetDate desc";
		logger.debug("sql={}", sql);
		try {
			return (ArrayList<Note>) getJdbcTemplate().query(
					sql,
					(rs, rowNum) -> new Note(rs.getString("noteId"), rs.getString("noteText"), rs
							.getString("targetDate"), rs.getString("userId"), rs.getString("groupId"), rs
							.getString("userName"), rs.getString("groupName"), rs.getInt("commentCount")), userId);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Note>();
		}
	}
}