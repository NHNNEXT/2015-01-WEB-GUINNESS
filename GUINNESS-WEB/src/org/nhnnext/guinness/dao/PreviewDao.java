package org.nhnnext.guinness.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.Preview;
import org.nhnnext.guinness.model.User;
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
				attentionList.toString(), questionList.toString());
	}
	
	public List<Preview> initReadPreviews(String groupId) {
		String sql = "select p.*, n.noteTargetDate, n.commentCount, u.userId, u.userName, u.userImage "
				+ "from previews p "
				+ "join notes n on p.noteId = n.noteId "
				+ "join users u on u.userId = n.userId "
				+ "where p.groupId = ? "
				+ "and n.noteTargetDate < now() "
				+ "order by n.noteTargetDate desc limit 10";
		try {
			return getJdbcTemplate().query(sql, (rs, rowNum) -> new Preview(
					new Note(rs.getString("noteId"), rs.getString("noteTargetDate"), rs.getInt("commentCount")),
					new User(rs.getString("userId"), rs.getString("userName"), rs.getString("userImage")),
					new Group(rs.getString("groupId")),
					rs.getString("attentionText"), 
					rs.getString("questionText")
					), groupId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Map<String, Object>> reloadPreviews(String groupId, long noteTargetDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("select p.*, n.commentCount, u.userId, u.userName, u.userImage ");
		sql.append("from previews p ");
		sql.append("join notes n on p.noteId = n.noteId ");
		sql.append("join users u on u.userId = n.userId ");
		sql.append("where p.groupId = ? ");
		sql.append("and n.noteTargetDate < now() ");
		if ( noteTargetDate != 0) sql.append("and n.noteTargetDate < '"+ noteTargetDate + "' ");
		sql.append("order by createDate desc limit 10");
		try {
			return getJdbcTemplate().queryForList(sql.toString(), groupId);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Map<String, Object>>();
		}
	}

	public int update(String noteId, String updateDate, ArrayList<String> attentionList, ArrayList<String> questionList) {
		String sql = "update PREVIEWS set attentionText = ?, questionText = ?, createDate = ? where noteId = ?";
		return getJdbcTemplate().update(sql, new Gson().toJson(attentionList), new Gson().toJson(questionList), updateDate, noteId);
	}
}
