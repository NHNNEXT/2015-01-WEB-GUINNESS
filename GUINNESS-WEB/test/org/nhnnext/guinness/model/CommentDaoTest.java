package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.nhnnext.guinness.controller.notes.ReadNoteListServlet;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);
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
		for(Iterator<Comment> i =  comments.iterator(); i.hasNext(); ) {
			Comment comment = i.next();
			logger.debug(comment.toString());
		}
	}

}
