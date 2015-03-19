package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

public class UserDAOTest {

	@Test
	public void readUserTest() throws SQLException {
		String userId = "userId1";
		UserDAO userDao = new UserDAO();
		assertNotNull( userDao.readUser(userId) );
	}

}
