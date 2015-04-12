package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UserDaoTest {

	@Test
	public void NotExistCreateUserTest() throws ClassNotFoundException {
		UserDao userDao = UserDao.getInstance();
		assertNotNull(userDao.readUser("test@naver.com"));
	}
}
