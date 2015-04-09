package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.common.AbstractDao;
import org.nhnnext.guinness.controller.notes.ReadNoteListServlet;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao extends AbstractDao {
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);
	private static UserDao userDao = new UserDao();
	
	public static UserDao getInstance() {
		return userDao;
	}
	
	public Boolean createUser(User user) throws SQLException {
		if (readUser(user.getUserId()) != null) {
			logger.debug("존재하는 userId 입니다!");
			return false;
		}

		String sql = "insert into USERS values(?,?,?,?,default)";
		queryNotForReturn(sql, user.getUserId(), user.getUserName(), user.getUserPassword(), null);
		return true;
	}

	public User readUser(String userId) throws SQLException, MakingObjectListFromJdbcException {
		String sql = "select * from USERS where userId=?";
		String[] params = { "userId", "userName", "userPassword"};
		List<?> list = queryForReturn(User.class, params, sql, userId);
		// TODO list.isEmpty() 메서드 활용
		if (list.size() != 0) {
			return (User) list.get(0);
		}
		return null;
	}
}
