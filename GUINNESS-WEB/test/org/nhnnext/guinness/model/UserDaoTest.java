package org.nhnnext.guinness.model;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;

public class UserDaoTest {

	@Test
	public void NotExistCreateUserTest() throws SQLException {
		User user = new User("testUserId2", "testUserName", "testUserPassword");
		UserDao userDao = new UserDao();
		userDao.createUser(user);
		assertEquals(userDao.checkExistUserId(user.getUserId()), true);
	}
}
