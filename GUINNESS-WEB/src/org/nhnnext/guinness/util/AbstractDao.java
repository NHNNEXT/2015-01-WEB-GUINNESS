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

import org.nhnnext.guinness.exception.DataAccessException;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDao {
	private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);

	protected Connection getConnection() throws DataAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		logger.debug("getConnection");
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/GUINNESS", "link413", "link413");
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	/**
	 * 리턴 없는 쿼리 실행 시
	 * 
	 * @param sql
	 *            쿼리문
	 * @param parameters
	 *            sql 쿼리의 인자
	 * @throws ClassNotFoundException
	 */
	public void queryNotForReturn(String sql, String... parameters) throws DataAccessException, ClassNotFoundException {
		Connection conn = null;
		conn = getConnection();
		try {
			PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
			pstmt.executeUpdate();
			terminateResources(conn, pstmt);
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	/**
	 * 갯수 리턴이 필요한 쿼리 실행 시
	 * 
	 * @param sql
	 *            쿼리문
	 * @param parameters
	 *            sql 쿼리의 인자
	 * @return
	 * @throws ClassNotFoundException
	 */
	public int queryForCountReturn(String sql, String... parameters) throws DataAccessException, ClassNotFoundException {
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		try {
			ResultSet rs = pstmt.executeQuery();
			rs.last();
			int result = rs.getRow();
			terminateResources(conn, pstmt);
			return result;
		} catch (SQLException e) {
			throw new DataAccessException();
		}
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
	 * @throws ClassNotFoundException
	 */
	public List<?> queryForObjectsReturn(Class<?> cls, String[] params, String sql, String... parameters)
			throws DataAccessException, MakingObjectListFromJdbcException, ClassNotFoundException {
		Connection conn = getConnection();
		PreparedStatement pstmt = setPreparedStatement(conn, sql, parameters);
		try {
			ResultSet rs = pstmt.executeQuery();
			List<?> array = getListObject(cls, params, rs);
			terminateResources(conn, pstmt, rs);
			return array;
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	private PreparedStatement setPreparedStatement(Connection conn, String sql, String... parameters)
			throws DataAccessException {
		int index = 1;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (String parms : parameters)
				pstmt.setString(index++, parms);
			return pstmt;
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	private List<Object> getListObject(Class<?> cls, String[] paramsKey, ResultSet rs)
			throws MakingObjectListFromJdbcException {
		List<Object> list = new ArrayList<Object>();
		try {
			Class<?>[] fields = extractClassFieldType(cls, paramsKey);
			Constructor<?> ct = cls.getConstructor(fields);
			while (rs.next()) {
				list.add(ct.newInstance(setParameterValuesOfConstructor(fields, rs, paramsKey)));
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchFieldException | SecurityException | NoSuchMethodException e) {
			throw new MakingObjectListFromJdbcException(e);
		} catch (SQLException e) {
			throw new DataAccessException();
		}
		return list;
	}

	private Object[] setParameterValuesOfConstructor(Class<?>[] fields, ResultSet rs, String[] paramsKey) {
		int sizeOfParam = paramsKey.length;
		Object[] paramsValue = new Object[sizeOfParam];
		for (int indexOfcolumn = 0; indexOfcolumn < sizeOfParam; indexOfcolumn++) {
			paramsValue[indexOfcolumn] = getParameterValue(fields[indexOfcolumn].getSimpleName().toString(), rs,
					paramsKey[indexOfcolumn]);
		}
		return paramsValue;
	}

	private Object getParameterValue(String fieldType, ResultSet rs, String paramsKey) throws NumberFormatException,
			DataAccessException {
		try {
			switch (fieldType) {
			case "String":
				return rs.getString(paramsKey);
			case "char":
				return rs.getString(paramsKey).charAt(0);
			case "byte":
				return rs.getByte(paramsKey);
			default:
				throw new MakingObjectListFromJdbcException();
			}
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	private Class<?>[] extractClassFieldType(Class<?> cls, String[] paramsKey) throws NoSuchFieldException {
		int sizeOfParam = paramsKey.length;
		Class<?>[] fields = new Class[sizeOfParam];
		for (int i = 0; i < sizeOfParam; i++)
			fields[i] = cls.getDeclaredField(paramsKey[i]).getType();
		if (fields.length == 0)
			throw new NoSuchFieldException();
		return fields;
	}

	protected void terminateResources(Connection conn, PreparedStatement pstmt, ResultSet rs)
			throws DataAccessException {
		try {
			if (conn != null)
				conn.close();
			if (pstmt != null)
				pstmt.close();
			if (rs != null)
				rs.close();
			logger.debug("terminateResources");
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	protected void terminateResources(Connection conn, PreparedStatement pstmt) {
		terminateResources(conn, pstmt, null);
	}
}
