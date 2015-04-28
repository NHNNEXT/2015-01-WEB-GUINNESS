package org.nhnnext.guinness.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ConfirmDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(ConfirmDao.class);

	public void createConfirm(String keyAddress, String userId) {
		String sql = "insert into CONFIRMS values(?,?,default)";
		getJdbcTemplate().update(sql, keyAddress, userId);
	}
}
