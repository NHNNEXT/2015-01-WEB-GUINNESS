package org.nhnnext.guinness.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
		RowMapper<User> rowMapper = new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString("userId"), rs.getString("userName"), rs.getString("userPassword"));
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rowMapper, userId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
