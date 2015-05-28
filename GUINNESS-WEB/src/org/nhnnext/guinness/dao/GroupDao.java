package org.nhnnext.guinness.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(GroupDao.class);

	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public void createGroup(Group group) {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?, ?)";
		getJdbcTemplate().update(sql, group.getGroupId(), group.getGroupName(), group.getGroupCaptainUserId(),
				group.getStatus(), group.getGroupImage());
	}

	public void deleteGroup(String groupId) {
		String sql = "delete from GROUPS where groupId=?";
		getJdbcTemplate().update(sql, groupId);
	}

	public void createGroupUser(String userId, String groupId) {
		String sql = "insert into GROUPS_USERS values(?,?)";
		getJdbcTemplate().update(sql, userId, groupId);
	}
	
	public void deleteGroupUser(String userId, String groupId) {
		String sql = "delete from GROUPS_USERS where userId=? and groupId=?";
		getJdbcTemplate().update(sql, userId, groupId);
	}

	public Group readGroup(String groupId) {
		String sql = "select * from GROUPS where groupId=?";

		try {
			return getJdbcTemplate().queryForObject(
					sql,
					(rs, rowNum) -> new Group(rs.getString("groupId"), rs.getString("groupName"), rs
							.getString("groupCaptainUserId"), rs.getString("status"), rs.getString("groupImage")), groupId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Map<String, Object>> readGroupListForMap(String userId) {
		String sql = "select * from GROUPS as G, (select groupId from GROUPS_USERS as A, USERS as B where A.userId = B.userId and B.userId = ?) as C where G.groupId = C.groupId ORDER BY groupName;";
		return getJdbcTemplate().queryForList(sql, userId);
	}

	public boolean checkJoinedGroup(String userId, String groupId) {
		String sql = "select count(*) from GROUPS_USERS where userId = ? and groupId = ?";
		if (getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { userId, groupId }) > 0)
			return true;
		return false;
	}

	public List<Map<String, Object>> readGroupMemberForMap(String groupId) {
		String sql = "select * from USERS,GROUPS_USERS where GROUPS_USERS.groupId = ? and GROUPS_USERS.userId = USERS.userId ORDER BY userName;";
		return getJdbcTemplate().queryForList(sql, groupId);
	}

	public List<User> readGroupMember(String groupId) {
		String sql = "select * from USERS,GROUPS_USERS where GROUPS_USERS.groupId = ? and GROUPS_USERS.userId = USERS.userId ORDER BY userName;";

		return getJdbcTemplate().query(
				sql,
				(rs, rowNum) -> new User(rs.getString("userId"), rs.getString("userName"),
						rs.getString("userPassword"), rs.getString("userStatus")), groupId);
	}

	public boolean isExistGroupId(String groupId) {
		String sql = "select count(1) from GROUPS where groupId = ?";
		logger.debug("{}", "" + getJdbcTemplate().queryForObject(sql, Integer.class, groupId));
		if (getJdbcTemplate().queryForObject(sql, Integer.class, groupId) == 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Group readGroupByNoteId(String noteId) {
		String sql = "select * from groups where groupId = "
				+ "(select distinct(groupId) from notes where noteId =?)";

		try {
			return getJdbcTemplate().queryForObject(
					sql,
					(rs, rowNum) -> new Group(rs.getString("groupId"), rs.getString("groupName"), rs
							.getString("groupCaptainUserId"), rs.getString("status"), rs.getString("groupImage")), noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateGroup(Group group) {
		String sql = "update GROUPS set groupName = ?, groupCaptainUserId = ?, status = ?, groupImage = ? where groupId = ?";
		getJdbcTemplate().update(sql, group.getGroupName(), group.getGroupCaptainUserId(), group.getStatus(), group.getGroupImage(), group.getGroupId());
	}
	
	public List<Map<String, Object>> searchQueryForMap (String... keywords) {
		String query = "";
		for (String keyword : keywords) {
			query += " OR groupName like \"%" + keyword + "%\"";
		}
		
		String sql = "SELECT * FROM GROUPS AS G WHERE ("+ query.substring(3) +") AND status = 'T'";
		try {
			return getJdbcTemplate().queryForList(sql);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Map<String, Object>>();
		}
	}

	public String findGroupCaptianUserId(String groupId) {
		String sql = "select * from GROUPS where groupId = ?";
		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Group(rs.getString("groupId"), rs.getString("groupName"), rs
					.getString("groupCaptainUserId"), rs.getString("status"), rs.getString("groupImage")), groupId).getGroupCaptainUserId();
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
