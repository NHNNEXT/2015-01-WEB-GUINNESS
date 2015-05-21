package org.nhnnext.guinness.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

@Repository
public class PreviewDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;
 
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public int create(Note note, Group group, ArrayList<String> attentionList, ArrayList<String> questionList) {
		String sql = "insert into PREVIEWS (noteId, groupId, attentionText, questionText) values(?, ?, ?, ?)";
		return getJdbcTemplate().update(sql, note.getNoteId(), group.getGroupId(), 
				new Gson().toJson(attentionList), new Gson().toJson(questionList));
	}
	
	public int create(String noteId, String groupId, ArrayList<String> attentionList, ArrayList<String> questionList) {
		String sql = "insert into PREVIEWS (noteId, groupId, attentionText, questionText) values(?, ?, ?, ?)";
		return getJdbcTemplate().update(sql, noteId, groupId, 
				new Gson().toJson(attentionList), new Gson().toJson(questionList));
	}

	public List<Map<String, Object>> readPreviewsForMap(String groupId) {
		String sql = "select p.*, n.commentCount, u.userId, u.userName, u.userImage "
				+ "from previews p "
				+ "join notes n on p.noteId = n.noteId "
				+ "join users u on u.userId = n.userId "
				+ "where p.groupId = ? order by createDate desc";
		return getJdbcTemplate().queryForList(sql, groupId);
	}
	
	public List<Map<String, Object>> readNotes(String groupId, String noteTargetDate, String userIds) {
		String sql = "select * from NOTES, USERS where NOTES.groupId = ? and NOTES.userId = USERS.userId ";
		if (userIds != null && userIds != "null" && userIds != "" ) {
			sql += "and NOTES.userId in (" + userIds + ") ";
		}
		if ( noteTargetDate != null) {
			sql += "and NOTES.noteTargetDate < '"+ noteTargetDate + "' ";
		}
		sql += "order by NOTES.noteTargetDate desc limit 3";
		try {
			return getJdbcTemplate().queryForList(sql, groupId);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Map<String, Object>>();
		}
	}
	
	public List<Map<String, Object>> readPreviews(String groupId) {
		String sql = "select attentionText, questionText from PREVIEWS, NOTES where PREVIEWS.noteId = NOTES.noteId and NOTES.groupId = ? order by NOTES.noteTargetDate desc";
		try {
			return getJdbcTemplate().queryForList(sql, groupId);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Map<String, Object>>();
		}
	}
}
