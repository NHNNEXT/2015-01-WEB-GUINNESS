package org.nhnnext.guinness.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.SessionUser;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public long createComment(Comment comment) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into COMMENTS (commentText, userId, noteId) values(?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "commentId" });
				ps.setString(1, comment.getCommentText());
				ps.setString(2, comment.getUser().getUserId());
				ps.setString(3, comment.getNote().getNoteId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
		
	}

	public List<Map<String, Object>> readCommentListByNoteId(String noteId) {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND noteId = ?";
		return getJdbcTemplate().queryForList(sql, noteId);
	}

	public Comment readCommentByCommentId(String commentId) {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND commentId = ?";

		return getJdbcTemplate().queryForObject(
				sql,
				(rs, rowNum) -> new Comment(rs.getString("commentId"), rs.getString("commentText"), rs
						.getString("commentCreateDate"), new SessionUser(rs.getString("userId"), rs
						.getString("userName"), rs.getString("userImage")), new Note(rs.getString("noteId"))),
				commentId);
	}

	public void deleteAllCommentsByNoteId(String noteId) {
		String sql = "delete from COMMENTS where noteId = ?";
		getJdbcTemplate().update(sql, noteId);
	}

	public void deleteComment(String commentId) {
		String sql = "delete from COMMENTS where commentId = ?";
		getJdbcTemplate().update(sql, commentId);
	}

	public void updateComment(String commentId, String commentText) {
		String sql = "update COMMENTS set commentText = ?, commentCreateDate = now() where commentId = ?";
		getJdbcTemplate().update(sql, commentText, commentId);
	}
}
