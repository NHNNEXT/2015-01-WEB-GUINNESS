package org.nhnnext.guinness.dao;

import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class UserDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	public void createUser(User user) {
		logger.debug("user: {}", user);
		String sql = "insert into USERS values(?,?,?,default,default,default)";
		getJdbcTemplate().update(sql, user.getUserId(), user.getUserName(), user.getUserPassword());
	}

	public User findUserByUserId(String userId) {
		logger.debug("findUserByUserId// userId: {}", userId);
		String sql = "select * from USERS where userId=?";

		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new User(
					rs.getString("userId"), 
					rs.getString("userName"), 
					rs.getString("userPassword"),
					rs.getString("userStatus"),
					rs.getString("userImage")
					), userId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public void updateUser(User user) {
		String sql = "update USERS set userName = ?, userPassword = ?, userImage = ? where userId = ?";
		getJdbcTemplate().update(sql, user.getUserName(), user.getUserPassword(), user.getUserImage(), user.getUserId());
	}

	public void updateUserState(String userId, String userStatus) {
		String sql = "update USERS set userStatus = ? where userId = ?";
		getJdbcTemplate().update(sql, userStatus, userId);	
	}
}
