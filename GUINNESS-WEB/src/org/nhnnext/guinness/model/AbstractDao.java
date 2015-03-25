package org.nhnnext.guinness.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao {

	Connection conn;
	
	protected Connection getConnection() throws ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/GUINNESS";
		String id = "link413";
		String pw = "link413";
		
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url, id, pw);
	}

	public void queryNotForReturn(String sql, Object... objects)
			throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = setPreparedStatement(sql, objects);
		pstmt.executeUpdate();
		terminateConnection(pstmt, null);
	}

	public List<Map<String, Object>> queryForReturn(String sql,
			Object... objects) throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = setPreparedStatement(sql, objects);
		ResultSet rs = pstmt.executeQuery();
		List<Map<String, Object>> list = getResultMapRows(rs);
		terminateConnection(pstmt, rs);
		return list;
	}

	private PreparedStatement setPreparedStatement(String sql,
			Object... objects) throws SQLException, ClassNotFoundException {
		int index = 1;
		conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (Object obj : objects) {
			switch (obj.getClass().getSimpleName().toString()) {
			case "String":
				pstmt.setString(index++, (String) obj);
				break;
			case "Integer":
				pstmt.setInt(index++, (int) obj);
				break;
			}
		}
		return pstmt;
	}

	private List<Map<String, Object>> getResultMapRows(ResultSet rs)
			throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int sizeOfColumn = metaData.getColumnCount();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String column = null;

		while (rs.next()) {
			map = new HashMap<String, Object>();
			for (int indexOfcolumn = 0; indexOfcolumn < sizeOfColumn; indexOfcolumn++) {
				column = metaData.getColumnName(indexOfcolumn + 1);
				map.put(column, rs.getString(column));
			}
			list.add(map);
		}
		return list;
	}
	
	protected void terminateConnection(PreparedStatement pstmt, ResultSet rs) throws SQLException {
		if (conn != null) conn.close();
		if (pstmt != null) pstmt.close();
		if (rs != null) rs.close();
	}
}