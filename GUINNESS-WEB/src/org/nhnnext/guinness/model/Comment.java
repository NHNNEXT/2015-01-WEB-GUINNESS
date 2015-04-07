package org.nhnnext.guinness.model;

public class Comment {
	private String commentText;
	private String commentType;
	private String userId;
	private String noteId;

	public Comment(String commentText, String commentType, String userId, String noteId) {
		this.commentText = commentText;
		this.commentType = commentType;
		this.userId = userId;
		this.noteId = noteId;
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
	
	@Override
	public String toString() {
		return "Comment [commentText=" + commentText + ", commentType=" + commentType + ", userId=" + userId
				+ ", noteId=" + noteId + "]";
	}
}
