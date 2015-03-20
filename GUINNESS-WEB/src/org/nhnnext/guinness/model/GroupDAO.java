package org.nhnnext.guinness.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO {

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
	
	public void executeSql(String code, Group group) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			
			switch(code) {
			case "create" :
				pstmt = conn.prepareStatement("insert into GROUPS values(?,?,?,DEFAULT,?)");
				pstmt.setString(1, group.getGroupId());
				pstmt.setString(2, group.getGroupName());
				pstmt.setString(3, group.getGroupCaptainUserId());
				pstmt.setInt(4, group.isPublic());
				break;
			case "remove" :
				pstmt = conn.prepareStatement("delete from GROUPS where groupId=?");
				pstmt.setString(1, group.getGroupId());
				break;
			}
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				pstmt.close();

			if (conn != null)
				conn.close();
		}
	}
	public void createGroup(Group group) throws SQLException {
		executeSql("create", group);
//		String sql = "insert into GROUPS values(?,?,?,DEFAULT,?)";
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			conn = getConnection();
//			pstmt = conn.prepareStatement(sql);
//
//			pstmt.setString(1, group.getGroupId());
//			pstmt.setString(2, group.getGroupName());
//			pstmt.setString(3, group.getGroupCaptainUserId());
//			pstmt.setInt(4, group.isPublic());
//			pstmt.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (pstmt != null)
//				pstmt.close();
//
//			if (conn != null)
//				conn.close();
//		}
	}
	
	public void removeGroup(Group group) throws SQLException {
		executeSql("remove", group);
//		String sql = "delete from GROUPS where groupId=?";
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//
//		try {
//			conn = getConnection();
//			pstmt = conn.prepareStatement(sql);
//
//			pstmt.setString(1, groupId);
//			pstmt.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (pstmt != null)
//				pstmt.close();
//
//			if (conn != null)
//				conn.close();
//		}
	}

	public boolean checkExistGroupId(String groupId) {
		String sql = "select groupId from GROUPS where groupId=?";
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		conn = getConnection();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();
			
			rs.last();      
	        int rowcount = rs.getRow();
			
			if(rowcount != 0){
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
				try {
					if(pstmt != null)
						pstmt.close();

					if(conn != null)
						conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
		}
		return result;
	}

	public Group findByGroupId(String groupId) throws SQLException {
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

		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
	}
}
