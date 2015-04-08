package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.nhnnext.guinness.controller.notes.ReadNoteListServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupTest {
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);
	public static Group TEST_GROUP = new Group("aIgpF", "테스트", "das@das.com", 'F');

	@Test
	public void testSetNewGroupId() throws Exception {
		String newGroupId = Group.setNewGroupId();
		logger.debug(newGroupId);
		assertNotNull(newGroupId);
	}
}
