package org.nhnnext.guinness.dao;

import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Comment;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class CommentDao extends JdbcDaoSupport {
	
	public void createComment(Comment comment) throws ClassNotFoundException {
		String sql = "insert into COMMENTS (commentText, commentType, userId, noteId, paragraphText) values(?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, comment.getCommentText(), comment.getCommentType(), comment.getUserId(), comment.getNoteId(), comment.getParagraphText());
	}

	public List<Comment> readCommentListByNoteId(String noteId) throws MakingObjectListFromJdbcException,
	ClassNotFoundException {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND noteId = ? AND commentType='A'";
		
		return getJdbcTemplate().query(sql, (rs, rowNum) -> new Comment(
				rs.getString("commentText"), 
				rs.getString("commentType"),
				rs.getString("createDate"), 
				rs.getString("userId"), 
				rs.getString("noteId"), 
				rs.getString("paragraphText"),
				rs.getString("userName"),
				rs.getString("commentId"),
				rs.getString("userImage")
				), noteId);
	}

	public Comment readCommentByCommentId(String commentId) {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND commentId = ?";
		
		return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Comment(
				rs.getString("commentText"), 
				rs.getString("commentType"), 
				rs.getString("createDate"), 
				rs.getString("userId"), 
				rs.getString("noteId"),
				rs.getString("paragraphText"),
				rs.getString("userName"),
				rs.getString("commentId")
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
