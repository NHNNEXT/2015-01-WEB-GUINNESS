package org.nhnnext.guinness.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nhnnext.guinness.model.Group;


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

	public void createGroup(Group group) throws SQLException {
		String sql = "insert into GROUPS values(?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, group.getGroupId());
			pstmt.setString(2, group.getGroupName());
			pstmt.setString(3, group.getGroupCaptainUserId());
			pstmt.setString(4, group.getCreateDate());
			pstmt.setInt(5, group.isPublic());

			System.out.println("restlt->" + pstmt.executeUpdate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(pstmt != null) 
				pstmt.close();

			if(conn != null)
				conn.close();
		}
	}
}
