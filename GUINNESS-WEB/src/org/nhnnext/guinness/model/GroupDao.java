package org.nhnnext.guinness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.DataAccessException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.util.AbstractDao;
import org.nhnnext.guinness.util.ObjectMapper;

public class GroupDao extends AbstractDao {
	private static GroupDao groupDao = new GroupDao();

	private GroupDao() {

	}

	public static GroupDao getInstance() {
		return groupDao;
	}

	public void createGroup(Group group) throws ClassNotFoundException {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
		queryNotForReturn(sql, group.getGroupId(), group.getGroupName(), group.getGroupCaptainUserId(),
				"" + group.isPublic());
	}

	public void deleteGroup(Group group) throws ClassNotFoundException {
		String sql = "delete from GROUPS where groupId=?";
		queryNotForReturn(sql, group.getGroupId());
	}

	public void createGroupUser(String userId, String groupId) throws ClassNotFoundException {
		String sql = "insert into GROUPS_USERS values(?,?)";
		queryNotForReturn(sql, userId, groupId);
	}

	public Group readGroup(String groupId) throws DataAccessException, ClassNotFoundException {
		String sql = "select * from GROUPS where groupId=?";
		ObjectMapper<Group> om = new ObjectMapper<Group>() {
			@Override
			public Group returnObject(ResultSet rs) throws SQLException {
				return new Group(rs.getString("groupId"), rs.getString("groupName"),
						rs.getString("groupCaptainUserId"), rs.getString("isPublic").charAt(0));
			}
		};
		List<?> list = queryForObjectsReturn(om, sql, groupId);
		if (!list.isEmpty())
			return (Group) list.get(0);
		return null;
	}

	// Group List를 받아오기 위함
	@SuppressWarnings("unchecked")
	public List<Group> readGroupList(String userId) throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId ORDER BY groupName;";
		ObjectMapper<Group> om = new ObjectMapper<Group>() {
			@Override
			public Group returnObject(ResultSet rs) throws SQLException {
				return new Group(rs.getString("groupId"), rs.getString("groupName"),
						rs.getString("groupCaptainUserId"), rs.getString("isPublic").charAt(0));
			}
		};
		List<?> list = queryForObjectsReturn(om, sql, userId);
		return (List<Group>) list;
	}

	public boolean checkJoinedGroup(String userId, String groupId) throws ClassNotFoundException {
		String sql = "select * from GROUPS_USERS, GROUPS where GROUPS_USERS.userId = ? and GROUPS_USERS.groupID = GROUPS.groupId and GROUPS.groupId = ?";
		if (queryForCountReturn(sql, userId, groupId) > 0)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<User> readGroupMember(String groupId) throws MakingObjectListFromJdbcException, ClassNotFoundException {
		String sql = "select * from USERS,GROUPS_USERS where GROUPS_USERS.groupId = ? and GROUPS_USERS.userId = USERS.userId;";
		ObjectMapper<User> om = new ObjectMapper<User>() {
			@Override
			public User returnObject(ResultSet rs) throws SQLException {
				return new User(rs.getString("userId"), rs.getString("userName"), rs.getString("userPassword"));
			}
		};
		List<?> list = queryForObjectsReturn(om, sql, groupId);
		return (List<User>) list;
	}
}
