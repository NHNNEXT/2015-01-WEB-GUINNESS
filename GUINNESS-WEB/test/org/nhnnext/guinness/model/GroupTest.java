package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GroupTest {
	
	public static Group TEST_GROUP = new Group("abcde", "testGroup", "test@guinness.org", 1);

	@Test
	public void testSetNewGroupId() throws Exception {
		String newGroupId = Group.setNewGroupId();
		System.out.println(newGroupId);
		assertNotNull(newGroupId);
	}
}
