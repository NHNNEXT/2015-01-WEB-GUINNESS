package org.nhnnext.guinness.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDAO {

	ResultSet rs = null;
	Connection conn = null;
	PreparedStatement pstmt = null;

	public Connection getConnection() {

		String url = "jdbc:mysql://localhost:3306/GUINNESS";
		String id = "link413";
		String pw = "link413";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private void terminateConnection() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createGroup(Group group) {
		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, group.getGroupId());
			pstmt.setString(2, group.getGroupName());
			pstmt.setString(3, group.getGroupCaptainUserId());
			pstmt.setInt(4, group.isPublic());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
	}

	public void removeGroup(Group group) {
		String sql = "delete from GROUPS where groupId=?";

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, group.getGroupId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
	}

	public void createGroupUser(String groupCaptainUserId, String groupId) {
		String sql = "insert into GROUPS_USERS values(?,?)";

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupCaptainUserId);
			pstmt.setString(2, groupId);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
	}

	public boolean checkExistGroupId(String groupId) {
		String sql = "select groupId from GROUPS where groupId=?";
		boolean result = false;

		conn = getConnection();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();

			rs.last();
			int rowcount = rs.getRow();

			if (rowcount != 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
		return result;
	}

	public Group findByGroupId(String groupId) {
		String sql = "select * from GROUPS where groupId = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();

			if (!rs.next())
				return null;

			return new Group(rs.getString("groupId"),
					rs.getString("groupName"),
					rs.getString("groupCaptainUserId"), rs.getInt("isPublic"));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
		return null;
	}

	public ArrayList<Group> readGroupList(String userId) {
		String sql = "select A.groupId, B.groupName, B.groupCaptainUserId, B.isPublic "
					+ "from GROUPS_USERS as A inner join GROUPS as B on A.groupId = B.groupId "
					+ "where A.userId =?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Group> result = new ArrayList<Group>();

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				result.add(new Group(rs.getString("groupId"),rs.getString("groupName"), 
						rs.getString("groupCaptainUserId"),rs.getInt("isPublic")));
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			terminateConnection();
		}
		return null;
	}

}
