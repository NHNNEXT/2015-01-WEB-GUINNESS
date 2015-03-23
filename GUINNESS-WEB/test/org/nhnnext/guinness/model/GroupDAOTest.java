package org.nhnnext.guinness.model;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GroupDAOTest {

	private GroupDAO groupDao;
	
	@Before
	public void setup(){
		groupDao = new GroupDAO();
	}
	
	@Test
	public void testCheckExistGroupId() {
		assertEquals(groupDao.checkExistGroupId("abcde"), true);
	}
	
	@Test
	public void testCheckUnExistGroupId() {
		assertEquals(groupDao.checkExistGroupId("11111"), false);
	}
	
	@Test
	public void crud() throws SQLException {
		Group group = GroupTest.TEST_GROUP;
		
		groupDao.removeGroup(group);
		groupDao.createGroup(group);
		
		Group dbGroup = groupDao.findByGroupId(group.getGroupId());
		
		assertEquals(group, dbGroup);
	}
}
