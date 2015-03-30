package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.common.AbstractDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class UserDao extends AbstractDao {
	public Boolean createUser(User user) throws SQLException {
		String userId = user.getUserId();
		if (checkExistUserId(userId)) {
			System.out.println("존재하는 userId 입니다!");
			return false;
		}

		String sql = "insert into USERS values(?,?,?,?,default)";
		queryNotForReturn(sql, user.getUserId(), user.getUserName(), user.getUserPassword(), null);
		return true;
	}

	public boolean checkExistUserId(String userId) throws SQLException, MakingObjectListFromJdbcException {
		boolean result = false;
		String sql = "select * from USERS where userId=?";
		String[] params = { "userId", "userName", "userPassword"};
		List<?> list = queryForReturn(User.class, params, sql, userId);
		if (list.size() != 0)
			result = true;
		return result;
	}
	
	public boolean checkLogin(String userId, String userPassword) throws SQLException {
		String sql = "select * from USERS where userId=? and userPassword=?";
		if (queryForCountReturn(sql, userId, userPassword) > 0)
			return true;
		return false;
	}
}
