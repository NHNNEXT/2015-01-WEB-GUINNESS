package org.nhnnext.guinness.dao;

import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ConfirmDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(ConfirmDao.class);

	public void createConfirm(String keyAddress, String userId) {
		String sql = "insert into CONFIRMS values(?,?,default)";
		getJdbcTemplate().update(sql, keyAddress, userId);
	}

	public String findUserIdByKeyAddress(String keyAddress) {
		String sql = "select * from CONFIRMS A, USERS B where A.keyAddress = ? AND A.userId = B.userId";
		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new User(
					rs.getString("userId"), 
					rs.getString("userName"), 
					rs.getString("userPassword"),
					rs.getString("userStatus").charAt(0)
					), keyAddress).getUserId();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void completeConfirm(String keyAddress) {
		String sql = "delete from CONFIRMS where keyAddress = ?";
		getJdbcTemplate().update(sql, keyAddress);
	}

}
