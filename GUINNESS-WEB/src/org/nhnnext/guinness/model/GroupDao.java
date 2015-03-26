package org.nhnnext.guinness.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class GroupDao extends AbstractDao {

	public void createGroup(Group group) throws SQLException,
			ClassNotFoundException {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
		queryNotForReturn(sql, group.getGroupId(), group.getGroupName(),
				group.getGroupCaptainUserId(), "" + group.isPublic());
	}

	public void deleteGroup(Group group) throws SQLException,
			ClassNotFoundException {
		String sql = "delete from GROUPS where groupId=?";
		queryNotForReturn(sql, group.getGroupId());
	}

	public void createGroupUser(String groupCaptainUserId, String groupId)
			throws SQLException, ClassNotFoundException {
		String sql = "insert into GROUPS_USERS values(?,?)";
		queryNotForReturn(sql, groupCaptainUserId, groupId);
	}

	public boolean checkExistGroupId(String groupId) throws ClassNotFoundException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException
			{
		boolean result = false;
		String sql = "select groupId from GROUPS where groupId=?";
		String[] params= {"groupId", "groupName", "groupCaptainUserId", "isPublic"};
		List<?> list = queryForReturn(Group.class, params, sql, groupId);
		if (list.size() != 0)
			result = true;
		return result;
	}

	public Group findByGroupId(String groupId) throws ClassNotFoundException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		String sql = "select * from GROUPS where groupId = ?";
		String[] paramsKey= {"groupId", "groupName", "groupCaptainUserId", "isPublic"};
		List<?> list = queryForReturn(Group.class, paramsKey, sql, groupId);
		return (Group) list.get(0);
	}

	public List<Group> readGroupList(String userId) throws ClassNotFoundException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId";
		String[] paramsKey= {"groupId", "groupName", "groupCaptainUserId", "isPublic"};
		List<?> list = queryForReturn(Group.class, paramsKey, sql, userId);
		return (List<Group>) list;
	}

	public static class comGroupName implements Comparator<Group> {
		@Override
		public int compare(Group o1, Group o2) {
			return o1.getGroupName().compareTo(o2.getGroupName());
		}
	}

}
