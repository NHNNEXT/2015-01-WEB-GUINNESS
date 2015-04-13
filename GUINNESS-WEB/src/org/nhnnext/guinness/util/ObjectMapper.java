package org.nhnnext.guinness.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectMapper<T> {
	T returnObject(ResultSet rs) throws SQLException;
}
