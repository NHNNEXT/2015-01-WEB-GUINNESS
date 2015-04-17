package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.controller.notes.ReadNoteListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class CommentDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListController.class);
	private static final Comment COMMENT = new Comment("test comment text", "A", "admin@guinness.com", "12");
	private static final String NOTEID = "7";
	
	@Autowired
	private CommentDao commentDao;
	
	@Test
	public void createComment() throws ClassNotFoundException {
		Comment comment = new Comment("unit test", "A", "admin@guinness.com", "9");
		commentDao.createComment(comment);
		
		assertEquals(comment.getNoteId(), commentDao.readCommentListByNoteId("9").get(0).getNoteId());
		assertEquals(comment.getCommentText(), commentDao.readCommentListByNoteId("9").get(0).getCommentText());
		assertEquals(comment.getCommentType(), commentDao.readCommentListByNoteId("9").get(0).getCommentType());
	}

	@Test
	public void readCommentListByNoteId() throws ClassNotFoundException {
		List<Comment> comments = commentDao.readCommentListByNoteId(NOTEID);
		for (Iterator<Comment> i = comments.iterator(); i.hasNext();) {
			Comment comment = i.next();
			logger.debug(comment.toString());
		}
		assertNotNull(comments);
	}

}
