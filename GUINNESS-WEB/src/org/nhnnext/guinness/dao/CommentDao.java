package org.nhnnext.guinness.dao;

import java.util.List;
import java.util.Map;

import org.nhnnext.guinness.model.Comment;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.User;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class CommentDao extends JdbcDaoSupport {
	
	public void createComment(Comment comment) {
		String sql = "insert into COMMENTS (commentText, commentType, userId, noteId) values(?, ?, ?, ?)";
		getJdbcTemplate().update(sql, comment.getCommentText(), comment.getCommentType(), comment.getUser().getUserId(), comment.getNote().getNoteId());
	}

	public List<Map<String, Object>> readCommentListByNoteId(String noteId) {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND noteId = ?";
		return getJdbcTemplate().queryForList(sql, noteId);
	}

	public Comment readCommentByCommentId(String commentId) {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND commentId = ?";
		
		return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Comment(
				rs.getString("commentId"),
				rs.getString("commentText"), 
				rs.getString("commentType"), 
				rs.getString("commentCreateDate"), 
				new User(rs.getString("userId"),
						rs.getString("userName"), 
						rs.getString("userPassword"),
						rs.getString("userStatus"),
						rs.getString("userImage")), 
				new Note(rs.getString("noteId"))
				), commentId);
	}
	
	public void deleteAllCommentsByNoteId(String noteId){
		String sql = "delete from COMMENTS where noteId = ?";
		getJdbcTemplate().update(sql, noteId);
	}

	public void deleteComment(String commentId) {
		String sql = "delete from COMMENTS where commentId = ?";
		getJdbcTemplate().update(sql, commentId);
	}

	public void updateComment(String commentId, String commentText) {
		String sql = "update COMMENTS set commentText = ? where commentId = ?";
		getJdbcTemplate().update(sql, commentText, commentId);
	}
}
