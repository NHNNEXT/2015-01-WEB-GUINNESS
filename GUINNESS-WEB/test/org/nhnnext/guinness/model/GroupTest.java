package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GroupTest {
	
	public static Group TEST_GROUP = new Group("aIgpF", "테스트", "das@das.com", 'F');

	@Test
	public void testSetNewGroupId() throws Exception {
		String newGroupId = Group.setNewGroupId();
		System.out.println(newGroupId);
		assertNotNull(newGroupId);
	}
}
