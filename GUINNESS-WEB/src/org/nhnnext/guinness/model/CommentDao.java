package org.nhnnext.guinness.model;

import java.sql.SQLException;

import org.nhnnext.guinness.common.AbstractDao;

public class CommentDao extends AbstractDao {

	public void createcomment(Comment comment) throws SQLException {
		String query = "insert into COMMENTS (comentText, comentType, userId, noteId) values(?, ?, ?, ?)";
		queryNotForReturn(query, comment.getCommentText(), comment.getCommentType(), comment.getUserId(), comment.getNoteId());
	}
}
