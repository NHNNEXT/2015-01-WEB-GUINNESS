package org.nhnnext.guinness.model;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

public class UserDAOTest {

	@Test
	public void ExistCreateUserTest() throws SQLException {
		String sql = "insert into USERS values('userId1', 'userName', 'userPassword', null, 2015-03-19 17:47:56)";
	}
	
	@Test
	public void readUserTest() throws SQLException {
		String userId = "userId1";
		UserDAO userDao = new UserDAO();
		assertNotNull( userDao.readUser(userId) );
	}

}
