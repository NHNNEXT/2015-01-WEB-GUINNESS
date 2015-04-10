package org.nhnnext.guinness.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

	protected Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		logger.debug("getConnection");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/GUINNESS", "link413", "link413");
	}

	/**
	 * 리턴 없는 쿼리 실행 시
	 * 
	 * @param sql
	 *            쿼리문
	 * @param parameters
	 *            sql 쿼리의 인자
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void queryNotForReturn(String sql, String... parameters) throws SQLException, ClassNotFoundException {
		Connection conn = null;
		conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		pstmt.executeUpdate();
		terminateResources(conn, pstmt);
	}

	/**
	 * 갯수 리턴이 필요한 쿼리 실행 시
	 * 
	 * @param sql
	 *            쿼리문
	 * @param parameters
	 *            sql 쿼리의 인자
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int queryForCountReturn(String sql, String... parameters) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		ResultSet rs = pstmt.executeQuery();
		rs.last();
		int result = rs.getRow();
		terminateResources(conn, pstmt);
		return result;
	}

	/**
	 * 객체 리턴이 필요한 쿼리 실행 시
	 * 
	 * @param cls
	 *            리스트 객체의 클래스 타입
	 * @param params
	 *            cls의 생성
	 * @param sql
	 *            쿼리문
	 * @param parameters
	 *            sql 쿼리의 인자
	 * @return cls형 리스트 객체
	 * @throws MakingObjectListFromJdbcException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<?> queryForObjectsReturn(Class<?> cls, String[] params, String sql, String... parameters)
			throws SQLException, MakingObjectListFromJdbcException, ClassNotFoundException {
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		ResultSet rs = pstmt.executeQuery();
		List<?> array = getListObject(cls, params, rs);
		terminateResources(conn, pstmt, rs);
		return array;
	}

	private PreparedStatement setPreparedStatement(Connection conn, String sql, String... parameters)
			throws SQLException {
		int index = 1;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (String parms : parameters)
			pstmt.setString(index++, parms);
		return pstmt;
	}

	private List<Object> getListObject(Class<?> cls, String[] paramsKey, ResultSet rs)
			throws MakingObjectListFromJdbcException {
		int sizeOfParam = paramsKey.length;
		List<Object> list = new ArrayList<Object>();
		try {
			Class<?>[] fields = new Class[sizeOfParam];
			for (int i = 0; i < sizeOfParam; i++)
				fields[i] = cls.getDeclaredField(paramsKey[i]).getType();
			Constructor<?> ct = cls.getConstructor(fields);
			Object[] paramsValue = new Object[sizeOfParam];

			while (rs.next()) {
				for (int indexOfcolumn = 0; indexOfcolumn < sizeOfParam; indexOfcolumn++) {
					switch (fields[indexOfcolumn].getSimpleName().toString()) {
					case "String":
						paramsValue[indexOfcolumn] = rs.getString(paramsKey[indexOfcolumn]);
						break;
					case "char":
						paramsValue[indexOfcolumn] = rs.getString(paramsKey[indexOfcolumn]).charAt(0);
						break;
					case "byte":
						paramsValue[indexOfcolumn] = rs.getByte(paramsKey[indexOfcolumn]);
						break;
					default:
						throw new MakingObjectListFromJdbcException();
					}
				}
				list.add(ct.newInstance(paramsValue));
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SQLException | NoSuchFieldException | SecurityException | NoSuchMethodException e) {
			throw new MakingObjectListFromJdbcException(e);
		}
		return list;
	}

	protected void terminateResources(Connection conn, PreparedStatement pstmt, ResultSet rs) throws SQLException {
		if (conn != null)
			conn.close();
		if (pstmt != null)
			pstmt.close();
		if (rs != null)
			rs.close();
		logger.debug("terminateResources");
	}

	protected void terminateResources(Connection conn, PreparedStatement pstmt) throws SQLException {
		terminateResources(conn, pstmt, null);
	}
}
