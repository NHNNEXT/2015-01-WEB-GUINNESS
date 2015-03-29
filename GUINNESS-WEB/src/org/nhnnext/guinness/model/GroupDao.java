package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class GroupDao extends AbstractDao {

	public void createGroup(Group group) throws SQLException, ClassNotFoundException {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
		queryNotForReturn(sql, group.getGroupId(), group.getGroupName(), group.getGroupCaptainUserId(),
				"" + group.isPublic());
	}

	public void deleteGroup(Group group) throws SQLException, ClassNotFoundException {
		String sql = "delete from GROUPS where groupId=?";
		queryNotForReturn(sql, group.getGroupId());
	}

	public void createGroupUser(String groupCaptainUserId, String groupId) throws SQLException, ClassNotFoundException {
		String sql = "insert into GROUPS_USERS values(?,?)";
		queryNotForReturn(sql, groupCaptainUserId, groupId);
	}

	public boolean checkExistGroupId(String groupId) throws ClassNotFoundException, MakingObjectListFromJdbcException,
			SQLException {
		boolean result = false;
		String sql = "select groupId from GROUPS where groupId=?";
		String[] params = { "groupId", "groupName", "groupCaptainUserId", "isPublic" };
		List<?> list = queryForReturn(Group.class, params, sql, groupId);
		if (list.size() != 0)
			result = true;
		return result;
	}

	public Group findByGroupId(String groupId) throws ClassNotFoundException, MakingObjectListFromJdbcException,
			SQLException {
		String sql = "select * from GROUPS where groupId = ?";
		String[] paramsKey = { "groupId", "groupName", "groupCaptainUserId", "isPublic" };
		List<?> list = queryForReturn(Group.class, paramsKey, sql, groupId);
		return (Group) list.get(0);
	}

	@SuppressWarnings("unchecked")
	// Group List를 받아오기 위함
	public List<Group> readGroupList(String userId) throws ClassNotFoundException, MakingObjectListFromJdbcException,
			SQLException {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId ORDER BY groupName;";
		String[] paramsKey = { "groupId", "groupName", "groupCaptainUserId", "isPublic" };
		List<?> list = queryForReturn(Group.class, paramsKey, sql, userId);
		return (List<Group>) list;
	}

	public boolean checkJoinedGroup(String userId, String groupId) throws ClassNotFoundException, SQLException {
		String sql = "select * from GROUPS_USERS, GROUPS where GROUPS_USERS.userId = ? and GROUPS_USERS.groupID = GROUPS.groupId and GROUPS.groupId = ?";
		if (queryForCountReturn(sql, userId, groupId) > 0)
			return true;
		return false;
	}
}
