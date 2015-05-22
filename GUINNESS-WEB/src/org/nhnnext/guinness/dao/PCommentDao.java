package org.nhnnext.guinness.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.PComment;
import org.nhnnext.guinness.model.SessionUser;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class PCommentDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public void createPComment(PComment pComment) {
		String sql = "insert into PCOMMENTS (pId, userId, noteId, sameSenCount, sameSenIndex, pCommentText, selectedText) values(?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, pComment.getpId(), pComment.getSessionUser().getUserId(),
				pComment.getNote().getNoteId(), pComment.getSameSenCount(), pComment.getSameSenIndex(),
				pComment.getpCommentText(), pComment.getSelectedText());
	}
	
	public List<Map<String, Object>> readPCommentListByNoteId(String noteId) {
		String sql = "select * from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND noteId = ?";
		return getJdbcTemplate().queryForList(sql, noteId);
	}

	public PComment readPCommentByPCommentId(String pCommentId) {
		String sql = "select * from PCOMMENTS, USERS where PCOMMENTS.userId = USERS.userId AND pCommentId = ?";

		return getJdbcTemplate().queryForObject(sql,(rs, rowNum) -> new PComment(
						rs.getString("pCommentId"), 
						rs.getString("pId"), 
						rs.getString("sameSenCount"), 
						rs.getString("sameSenIndex"), 
						rs.getString("pCommentText"), 
						rs.getString("selectedText"), 
						rs.getString("commentCreateDate"), 
						new SessionUser(rs.getString("userId"), rs.getString("userName"), rs.getString("userImage")),
						new Note(rs.getString("noteId"))
						), pCommentId);
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
