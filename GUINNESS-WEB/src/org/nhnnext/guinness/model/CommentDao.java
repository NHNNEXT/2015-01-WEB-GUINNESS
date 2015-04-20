package org.nhnnext.guinness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class CommentDao extends JdbcDaoSupport {
	
	public void createComment(Comment comment) throws ClassNotFoundException {
		String sql = "insert into COMMENTS (commentText, commentType, userId, noteId) values(?, ?, ?, ?)";
		getJdbcTemplate().update(sql, comment.getCommentText(), comment.getCommentType(), comment.getUserId(), comment.getNoteId());
	}

	public List<Comment> readCommentListByNoteId(String noteId) throws MakingObjectListFromJdbcException,
			ClassNotFoundException {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND noteId = ?";
		return getJdbcTemplate().query(sql, new RowMapper<Comment>() {
			public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Comment(rs.getString("commentText"), rs.getString("commentType"), rs.getString("createDate"), rs.getString("userId"), rs.getString("noteId"), rs.getString("userName"));
			}
		}, noteId);
	}
}
