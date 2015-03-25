package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GroupDaoTest {

	private GroupDao groupDao;
	
	@Before
	public void setup(){
		groupDao = new GroupDao();
	}
	
	@Test
	public void testCheckExistGroupId() throws Exception {
		assertEquals(groupDao.checkExistGroupId("abcde"), true);
	}
	
	@Test
	public void testCheckUnExistGroupId() throws Exception {
		assertEquals(groupDao.checkExistGroupId("11111"), false);
	}
	
	@Test
	public void crud() throws SQLException, ClassNotFoundException {
		Group group = GroupTest.TEST_GROUP;
		
		groupDao.deleteGroup(group);
		groupDao.createGroup(group);
		
		Group dbGroup = groupDao.findByGroupId(group.getGroupId());
		assertEquals(group, dbGroup);
	}
	
	@Test
	public void readGroupList() throws ClassNotFoundException, SQLException {
		ArrayList<Group> list = groupDao.readGroupList("test@guinness.org");
		assertNotNull(list);
		System.out.println(list.size());
	}
}
