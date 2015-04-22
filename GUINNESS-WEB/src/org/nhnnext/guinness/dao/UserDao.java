package org.nhnnext.guinness.dao;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	public void createUser(User user) throws ClassNotFoundException, AlreadyExistedUserIdException {
		if (readUser(user.getUserId()) != null) {
			logger.debug("UserDao: userId already exist!");
			throw new AlreadyExistedUserIdException();
		}
		String sql = "insert into USERS values(?,?,?,?,default)";
		getJdbcTemplate().update(sql, user.getUserId(), user.getUserName(), user.getUserPassword(), null);
	}

	public User readUser(String userId) throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String sql = "select * from USERS where userId=?";

		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new User(
					rs.getString("userId"), 
					rs.getString("userName"), 
					rs.getString("userPassword")
					), userId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public void updateUser(User user) throws ClassNotFoundException {
		String sql = "update USERS set userName = ?, userPassword = ?, userImage = ? where userId = ?";
		getJdbcTemplate().update(sql, user.getUserName(), user.getUserPassword(), null, user.getUserId());
	}
}
