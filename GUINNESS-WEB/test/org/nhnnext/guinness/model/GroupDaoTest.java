package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class GroupDaoTest {

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private UserDao userDao;
	
	private User user = new User("das@das.com", "다스", "1q2w3e", (byte)0);
	private Group group = new Group("group", "groupName", "das@das.com", 'F');

	@Test
	public void createGroup() throws ClassNotFoundException {
		if (groupDao.readGroup(group.getGroupId()) != null)
			groupDao.deleteGroup(group);
		groupDao.createGroup(group);
		assertNotNull(group);
	}

	@Test
	public void readExistedGroup() {
		Group getGroup = groupDao.readGroup("ygNDa");
		assertNotNull(getGroup);
	}

	@Test
	public void readNotExistedGroup() {
		Group getGroup = groupDao.readGroup("aaaaa");
		assertNull(getGroup);
	}
	
	@Test
	public void createGroupUser() throws Exception {
		if(userDao.readUser(user.getUserId()) != null)
			userDao.createUser(user);
		if (groupDao.readGroup(group.getGroupId()) != null)
			groupDao.createGroup(group);
		
		groupDao.createGroupUser(user.getUserId(), group.getGroupId());
		
	}

}
