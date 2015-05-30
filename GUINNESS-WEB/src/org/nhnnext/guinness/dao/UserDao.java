package org.nhnnext.guinness.dao;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;
 
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	public void createUser(User user) {
		String sql = "insert into USERS values(?,?,?,default,default,default)";
		getJdbcTemplate().update(sql, user.getUserId(), user.getUserName(), user.getUserPassword());
	}

	public User findUserByUserId(String userId) {
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

	public void initPassword(User user) {
		String sql = "update USERS set userPassword = ? where userId = ?";
		getJdbcTemplate().update(sql, user.getUserPassword(), user.getUserId());
	}
}
