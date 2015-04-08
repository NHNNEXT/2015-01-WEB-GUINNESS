package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class CommentDaoTest {
	private static final Comment COMMENT = new Comment("test comment text", "A", "admin@guinness.com", "12");
	
	@Test
	public void createComment() throws SQLException {
		CommentDao commentDao = new CommentDao();
		commentDao.createcomment(COMMENT);
	}
	
	@Test
	public void readCommentListByNoteId() throws MakingObjectListFromJdbcException, SQLException {
		CommentDao commentDao = new CommentDao();
		List<Comment> comments = commentDao.readCommentListByNoteId("2");
		
		System.out.println(comments);
	}

}
