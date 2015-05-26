package org.nhnnext.guinness.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.TempNote;
import org.nhnnext.guinness.model.User;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TempNoteDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;
 
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public long create(TempNote tempNote) {
		String sql = "insert into TEMP_NOTES (noteText, createDate, userId) values(?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "noteId" });
				ps.setString(1, tempNote.getNoteText());
				ps.setString(2, tempNote.getCreateDate());
				ps.setString(3, tempNote.getUser().getUserId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}

	public List<TempNote> read(String userId) {
		String sql = "select *from TEMP_NOTES where userId = ?";
		return getJdbcTemplate().query(sql, (rs, rowNum) -> new TempNote(
				rs.getLong("noteId"), 
				rs.getString("noteText"), 
				rs.getString("createDate"), 
				new User(rs.getString("userId"))), userId);
	}

	public TempNote readByNoteId(String noteId) {
		String sql = "select * from TEMP_NOTES where noteId = ?";
		return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new TempNote(
				rs.getLong("noteId"), 
				rs.getString("noteText"), 
				rs.getString("createDate"), 
				new User(rs.getString("userId"))), noteId);
	}
}
