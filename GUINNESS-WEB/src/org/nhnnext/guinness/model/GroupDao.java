package org.nhnnext.guinness.model;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class GroupDao extends JdbcDaoSupport {

	public void createGroup(Group group) {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
		getJdbcTemplate().update(sql, group.getGroupId(), group.getGroupName(),
				group.getGroupCaptainUserId(), "" + group.isPublic());
	}

	public void deleteGroup(Group group) {
		String sql = "delete from GROUPS where groupId=?";
		getJdbcTemplate().update(sql, group.getGroupId());
	}

	public void createGroupUser(String userId, String groupId) {
		String sql = "insert into GROUPS_USERS values(?,?)";
		getJdbcTemplate().update(sql, userId, groupId);
	}

	public Group readGroup(String groupId) {
		String sql = "select * from GROUPS where groupId=?";
		
		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Group(
					rs.getString("groupId"), 
					rs.getString("groupName"), 
					rs.getString("groupCaptainUserId"), 
					rs.getString("isPublic").charAt(0)
					), groupId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Group> readGroupList(String userId) {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId ORDER BY groupName;";
		
		return getJdbcTemplate().query(sql, (rs, rowNum) -> new Group(rs.getString("groupId"),
				rs.getString("groupName"),
				rs.getString("groupCaptainUserId"), 
				rs.getString("isPublic").charAt(0)
				), userId);
	}

	public boolean checkJoinedGroup(String userId, String groupId) {
		String sql = "select count(*) from GROUPS_USERS, GROUPS where GROUPS_USERS.userId = ? and GROUPS_USERS.groupID = GROUPS.groupId and GROUPS.groupId = ?";
		if ( getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { userId, groupId }) > 0)
			return true;
		return false;
	}

	public List<User> readGroupMember(String groupId) {
		String sql = "select * from USERS,GROUPS_USERS where GROUPS_USERS.groupId = ? and GROUPS_USERS.userId = USERS.userId;";
		
		return getJdbcTemplate().query(sql,  (rs, rowNum) -> new User(
				rs.getString("userId"),
				rs.getString("userName"), 
				rs.getString("userPassword")
				), groupId);
	}
}
