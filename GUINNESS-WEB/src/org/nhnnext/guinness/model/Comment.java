package org.nhnnext.guinness.model;

public class Comment {
	private String commentText;
	private String commentType;
	private String createDate;
	private String userId;
	private String noteId;
	private String userName;
	
	public Comment(String commentText, String commentType, String createDate, String userId, String noteId, String userName) {
		this.commentText = commentText;
		this.commentType = commentType;
		this.createDate = createDate;
		this.userId = userId;
		this.noteId = noteId;
		this.userName = userName;
	}
	
	public Comment(String commentText, String commentType, String createDate, String userId, String noteId) {
		this(commentText, commentType, createDate, userId, noteId, null);
	}
	public Comment(String commentText, String commentType, String userId, String noteId) {
		this(commentText, commentType, null, userId, noteId);
	}
	
	public String getCommentText() {
		return commentText;
	}
	
	public String getCommentType() {
		return commentType;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getNoteId() {
		return noteId;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	
	public String getUserName() {
		return userName;
	}
	
	@Override
	public String toString() {
		return "Comment [commentText=" + commentText + ", commentType=" + commentType + ", userId=" + userId
				+ ", noteId=" + noteId + "]";
	}
}
