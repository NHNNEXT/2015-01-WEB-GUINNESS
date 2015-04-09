package org.nhnnext.guinness.model;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;

public class UserDaoTest {

	@Test
	public void NotExistCreateUserTest() throws SQLException, ClassNotFoundException {
		UserDao userDao = new UserDao();
		assertNotNull(userDao.readUser("test@naver.com"));
	}
}
