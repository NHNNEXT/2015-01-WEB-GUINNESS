package org.nhnnext.guinness.model;

import java.sql.SQLException;

import org.junit.Test;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class GroupDaoTest {

	@Test
	public void readUserListByGroupId() throws MakingObjectListFromJdbcException, SQLException {
		GroupDao groupDao = new GroupDao();
		groupDao.readUserListByGroupId("gcPQN");
	}

}
