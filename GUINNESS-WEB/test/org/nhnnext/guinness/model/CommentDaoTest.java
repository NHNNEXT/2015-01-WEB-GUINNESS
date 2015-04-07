package org.nhnnext.guinness.model;

import java.sql.SQLException;

import org.junit.Test;

public class CommentDaoTest {
	private static final Comment COMMENT = new Comment("test comment text", "A", "admin@guinness.com", "12");
	
	@Test
	public void createComment() throws SQLException {
		CommentDao commentDao = new CommentDao();
		commentDao.createcomment(COMMENT);
	}

}
