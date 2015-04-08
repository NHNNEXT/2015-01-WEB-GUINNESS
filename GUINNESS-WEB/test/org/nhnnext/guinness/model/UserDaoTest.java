package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

public class UserDaoTest {

	@Test
	public void NotExistCreateUserTest() throws SQLException {
		UserDao userDao = new UserDao();
		assertNotNull(userDao.readUser("test@naver.com"));
	}
}
