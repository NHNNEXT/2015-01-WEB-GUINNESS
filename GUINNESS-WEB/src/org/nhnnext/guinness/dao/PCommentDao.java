package org.nhnnext.guinness.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PCommentDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(PCommentDao.class);
	
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public String createPComment(PComment pComment) {
		String sql = "insert into PCOMMENTS (pId, userId, noteId, sameSenCount, sameSenIndex, pCommentText, selectedText) values(?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] { "pCommentId" });
				ps.setString(1, pComment.getpId());
				ps.setString(2, pComment.getSessionUser().getUserId());
				ps.setString(3, pComment.getNote().getNoteId());
				ps.setString(4, pComment.getSameSenCount());
				ps.setString(5, pComment.getSameSenIndex());
				ps.setString(6, pComment.getpCommentText());
				ps.setString(7, pComment.getSelectedText());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().toString();
	}

	public List<PComment> readListByPId(String pId, String noteId) {
		String sql = "select * from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND pId = ? AND noteId = ?";
		List<PComment> pc = getJdbcTemplate().query(sql, (rs, rowNum) -> new PComment(rs.getString("pCommentId"), rs.getString("pId"),
						rs.getString("sameSenCount"), rs.getString("sameSenIndex"), rs.getString("pCommentText"),
						rs.getString("selectedText"), rs.getString("pCommentCreateDate"),
						new SessionUser(rs.getString("userId"), rs.getString("userName"), rs.getString("userImage")),
						new Note(rs.getString("noteId"))), pId, noteId);
		return pc;
	}
	
	public List<Map<String, Object>> readListByNoteId(String noteId) {
		String sql = "select * from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND noteId = ?";
		return getJdbcTemplate().queryForList(sql, noteId);
	}
	
	public List<Map<String, Object>> countByPGroupPCommnent(String noteId) {
		String sql = "select pId, count(1) from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND noteId = ? GROUP BY pId";
		return getJdbcTemplate().queryForList(sql, noteId);
	}


	public PComment readByPCommentId(String pCommentId) {
		String sql = "select * from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND pCommentId = ?";
		return getJdbcTemplate().queryForObject(
				sql,
				(rs, rowNum) -> new PComment(rs.getString("pCommentId"), rs.getString("pId"), rs
						.getString("sameSenCount"), rs.getString("sameSenIndex"), rs.getString("pCommentText"), rs
						.getString("selectedText"), rs.getString("pCommentCreateDate"), new SessionUser(rs
						.getString("userId"), rs.getString("userName"), rs.getString("userImage")), new Note(rs
						.getString("noteId"))), pCommentId);
	}

	public void deleteAllPCommentsByNoteId(String noteId) {
		String sql = "delete from PCOMMENTS where noteId = ?";
		getJdbcTemplate().update(sql, noteId);
	}

	public void deletePComment(String pCommentId) {
		String sql = "delete from PCOMMENTS where pCommentId = ?";
		getJdbcTemplate().update(sql, pCommentId);
	}

	public void updatePComment(String pCommentId, String pCommentText) {
		String sql = "update PCOMMENTS set pCommentText = ? where pCommentId = ?";
		getJdbcTemplate().update(sql, pCommentText, pCommentId);
	}

	public void updatePId(String pCommentId, String pId) {
		String sql = "update PCOMMENTS set pId = ? where pCommentId = ?";
		getJdbcTemplate().update(sql, pId, pCommentId);
	}
}
