package org.nhnnext.guinness.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;

public class UserDaoTest {

	@Test
	public void NotExistCreateUserTest() throws SQLException {
		User user = new User("testUserId2", "testUserName", "testUserPassword");
		UserDao userDao = new UserDao();
		userDao.createUser(user);
		User dbUser = userDao.readUser(user.getUserId());
		assertEquals(user.getUserId(), dbUser.getUserId());
		
	}
	
	@Test
	public void readUserTest() throws SQLException {
		String userId = "userId1";
		UserDao userDao = new UserDao();
		assertNotNull( userDao.readUser(userId) );
	}

}
