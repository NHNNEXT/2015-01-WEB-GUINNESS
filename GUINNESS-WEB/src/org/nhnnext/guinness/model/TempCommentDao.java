package org.nhnnext.guinness.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.util.AbstractDao;
import org.nhnnext.guinness.util.ObjectMapper;

public class TempCommentDao extends AbstractDao {
	private static TempCommentDao commentDao = new TempCommentDao();

	private TempCommentDao() {

	}

	public static TempCommentDao getInstance() {
		return commentDao;
	}

	public void createcomment(Comment comment) throws ClassNotFoundException {
		String query = "insert into COMMENTS (commentText, commentType, userId, noteId) values(?, ?, ?, ?)";
		queryNotForReturn(query, comment.getCommentText(), comment.getCommentType(), comment.getUserId(),
				comment.getNoteId());
	}

	@SuppressWarnings("unchecked")
	public List<Comment> readCommentListByNoteId(String noteId) throws MakingObjectListFromJdbcException,
			ClassNotFoundException {
		String sql = "select * from COMMENTS, USERS where COMMENTS.userId = USERS.userId AND noteId = ?;";
		ObjectMapper<Comment> om = new ObjectMapper<Comment>() {
			@Override
			public Comment returnObject(ResultSet rs) throws SQLException {
				return new Comment(rs.getString("commentText"), rs.getString("commentType"),
						rs.getString("createDate"), rs.getString("userId"), rs.getString("noteId"),
						rs.getString("userName"));
			}
		};
		List<?> list = queryForObjectsReturn(om, sql, noteId);
		return (List<Comment>) list;
	}

}
