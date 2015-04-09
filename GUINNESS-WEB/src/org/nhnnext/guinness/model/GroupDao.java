package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.common.AbstractDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class GroupDao extends AbstractDao {
	private static GroupDao groupDao = new GroupDao();
	
	public static GroupDao getInstance() {
		return groupDao;
	}
	public void createGroup(Group group) throws SQLException {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
		queryNotForReturn(sql, group.getGroupId(), group.getGroupName(), group.getGroupCaptainUserId(),
				"" + group.isPublic());
	}

	public void deleteGroup(Group group) throws SQLException {
		String sql = "delete from GROUPS where groupId=?";
		queryNotForReturn(sql, group.getGroupId());
	}

	public void createGroupUser(String userId, String groupId) throws SQLException {
		String sql = "insert into GROUPS_USERS values(?,?)";
		queryNotForReturn(sql, userId, groupId);
	}

	public Group readGroup(String groupId) throws MakingObjectListFromJdbcException, SQLException {
		String sql = "select * from GROUPS where groupId=?";
		String[] params = { "groupId", "groupName", "groupCaptainUserId", "isPublic" };
		List<?> list = queryForReturn(Group.class, params, sql, groupId);
		// TODO list.isEmpty() method 활용한다.
		if (list.size() != 0)
			return (Group) list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	// Group List를 받아오기 위함
	public List<Group> readGroupList(String userId) throws MakingObjectListFromJdbcException, SQLException {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId ORDER BY groupName;";
		String[] paramsKey = { "groupId", "groupName", "groupCaptainUserId", "isPublic" };
		List<?> list = queryForReturn(Group.class, paramsKey, sql, userId);
		return (List<Group>) list;
	}

	public boolean checkJoinedGroup(String userId, String groupId) throws SQLException {
		String sql = "select * from GROUPS_USERS, GROUPS where GROUPS_USERS.userId = ? and GROUPS_USERS.groupID = GROUPS.groupId and GROUPS.groupId = ?";
		if (queryForCountReturn(sql, userId, groupId) > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<User> readGroupMember(String groupId) throws MakingObjectListFromJdbcException, SQLException {
		String sql = "select * from USERS,GROUPS_USERS where GROUPS_USERS.groupId = ? and GROUPS_USERS.userId = USERS.userId;";
		String[] paramsKey = { "userId", "userName", "userPassword", "userImage" };
		List<?> list = queryForReturn(User.class, paramsKey, sql, groupId);
		return (List<User>) list;
	}
}
