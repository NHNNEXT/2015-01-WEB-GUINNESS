package org.nhnnext.guinness.model;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public abstract class AbstractDao {

	Connection conn;
	static Gson gson = new Gson();
	static Type groupList = new TypeToken<List<Group>>() {
	}.getType();

	protected Connection getConnection() throws ClassNotFoundException,
			SQLException {
		String url = "jdbc:mysql://localhost:3306/GUINNESS";
		String id = "link413";
		String pw = "link413";

		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url, id, pw);
	}

	public void queryNotForReturn(String sql, String... parameters)
			throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = setPreparedStatement(sql, parameters);
		pstmt.executeUpdate();
		terminateConnection(pstmt, null);
	}

	public String queryForReturn(String sql, String... parameters)
			throws SQLException, ClassNotFoundException {
		PreparedStatement pstmt = setPreparedStatement(sql, parameters);
		ResultSet rs = pstmt.executeQuery();
		JsonArray array = getResultMapRows(rs);
		terminateConnection(pstmt, rs);
		return array.toString();
	}

	private PreparedStatement setPreparedStatement(String sql,
			String... parameters) throws SQLException, ClassNotFoundException {
		int index = 1;
		conn = getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (String parms : parameters) {
			pstmt.setString(index++, parms);
		}
		return pstmt;
	}

	private JsonArray getResultMapRows(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int sizeOfColumn = metaData.getColumnCount();

		String column = null;
		JsonArray array = new JsonArray();
		JsonObject obj = new JsonObject();

		while (rs.next()) {
			for (int indexOfcolumn = 0; indexOfcolumn < sizeOfColumn; indexOfcolumn++) {
				column = metaData.getColumnName(indexOfcolumn + 1);
				obj.addProperty(column, rs.getString(column));
			}
			array.add(obj);
		}
		return array;
	}

	protected void terminateConnection(PreparedStatement pstmt, ResultSet rs)
			throws SQLException {
		if (conn != null)
			conn.close();
		if (pstmt != null)
			pstmt.close();
		if (rs != null)
			rs.close();
	}
}