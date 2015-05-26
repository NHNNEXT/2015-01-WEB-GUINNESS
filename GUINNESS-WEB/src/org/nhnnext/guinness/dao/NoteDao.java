package org.nhnnext.guinness.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.Preview;
import org.nhnnext.guinness.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;
 
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	public long createNote(Note note) {
		String sql = "insert into NOTES (noteText, noteTargetDate, userId, groupId, commentCount) values(?, ?, ?, ?, 0)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "noteId" });
				ps.setString(1, note.getNoteText());
				ps.setString(2, note.getNoteTargetDate());
				ps.setString(3, note.getUser().getUserId());
				ps.setString(4, note.getGroup().getGroupId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public int checkGroupNotesCount(String groupId) {
		String sql = "select count(1) from NOTES where groupId=?";

		return getJdbcTemplate().queryForObject(sql, Integer.class, groupId);
	}

	public Note readNote(String noteId) {
		String sql = "select * from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		try {
			return getJdbcTemplate().queryForObject(
					sql, (rs, rowNum) -> new Note(rs.getString("noteId"),
							rs.getString("noteText"),
							rs.getString("noteTargetDate"),
							new User(rs.getString("userId"), rs.getString("userName"), rs.getString("userPassword"), rs.getString("userStatus"), rs.getString("userImage")),
							new Group(rs.getString("groupId")),
							rs.getInt("commentCount")), noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void increaseCommentCount(String noteId) {
		String sql = "update NOTES set commentCount = commentCount + 1 where noteId = ?";
		getJdbcTemplate().update(sql, noteId);
	}

	public int decreaseCommentCount(String commentId) {
		String sql = "update NOTES set commentCount = commentCount - 1 where noteId = (select noteId from COMMENTS where commentId = ?)";
		return getJdbcTemplate().update(sql, Long.parseLong(commentId));
	}

	public int deleteNote(String noteId) {
		String sql = "delete from NOTES where noteId = ?";
		return getJdbcTemplate().update(sql, noteId);
	}

	public void updateNote(String text, String noteId, String noteTargetDate) {
		String sql = "UPDATE NOTES SET noteText = ?, noteTargetDate = ? where noteId = ?";
		getJdbcTemplate().update(sql, text, noteTargetDate, noteId);
	}

	public List<Map<String, Object>> searchQueryForMap(String userId, String... keywords) {
		String query = "";
		for (String keyword : keywords) {
			query += " OR N.noteText like \"%" + keyword + "%\"";
		}
		String sql = "SELECT distinct noteId, noteText, noteTargetDate, N.userId, N.groupId, U.userName, G.groupName, N.commentCount FROM NOTES N LEFT JOIN USERS U ON N.userId = U.userId LEFT JOIN GROUPS G ON N.groupId = G.groupId LEFT JOIN GROUPS_USERS GU on GU.groupId = N.groupId WHERE "
				+ query.substring(3)
				+ " and N.groupId in (select groupId from GROUPS_USERS where userId = ?) AND N.userId = GU.userId order by N.noteTargetDate desc";
		try {
			return getJdbcTemplate().queryForList(sql, userId);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Map<String, Object>>();
		}
	}

	public List<String> readNotesByDate(String groupId, String startDate, String lastDate) {
		String sql = "SELECT * FROM NOTES WHERE groupId = ? and noteTargetDate >= ? and noteTargetDate <= ?";
		return getJdbcTemplate().query(sql, (rs, rowNum) -> new String(rs.getString("noteTargetDate").substring(0, 19)), groupId, startDate, lastDate);
	}
}