package org.nhnnext.guinness.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;

public abstract class AbstractDao {
	static Gson gson = new Gson();
	static Type GroupList = new TypeToken<List<Group>>() {
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
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		pstmt.executeUpdate();
		terminateResources(conn, pstmt, null);
	}

	public List<?> queryForReturn(Class<?> cls, String[] params, String sql,
			String... parameters) throws SQLException, NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		ResultSet rs = pstmt.executeQuery();
		List<?> array = getResultMapRows(cls, params, rs);
		terminateResources(conn, pstmt, rs);
		return array;
	}

	private PreparedStatement setPreparedStatement(Connection conn, String sql,
			String... parameters) throws SQLException, ClassNotFoundException {
		int index = 1;
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (String parms : parameters)
			pstmt.setString(index++, parms);

		return pstmt;
	}

	private List<Object> getResultMapRows(Class<?> cls, String[] paramsKey, ResultSet rs) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		int sizeOfParam = paramsKey.length;
		List<Object> list = new ArrayList<Object>();
		Constructor<?> ct = cls.getConstructors()[0];
		Object[] paramsValue = new Object[sizeOfParam];
		Field[] fields = new Field[sizeOfParam];
		
		for(int i=0; i<sizeOfParam; i++) {
			fields[i]=cls.getDeclaredField(paramsKey[i]);
		}
		
		while(rs.next()) {
			for (int indexOfcolumn = 0; indexOfcolumn < sizeOfParam; indexOfcolumn++) {
				switch(fields[indexOfcolumn].getType().getSimpleName().toString()) {
				case "String":
					paramsValue[indexOfcolumn] = rs.getString(paramsKey[indexOfcolumn]);
					break;
				case "char":
					paramsValue[indexOfcolumn] = rs.getString(paramsKey[indexOfcolumn]).charAt(0);
					break;
				}
			}
			list.add(ct.newInstance(paramsValue));
		}
		return list;
	}

	protected void terminateResources(Connection conn, PreparedStatement pstmt,
			ResultSet rs) throws SQLException {
		if (conn != null)
			conn.close();
		if (pstmt != null)
			pstmt.close();
		if (rs != null)
			rs.close();
	}
}