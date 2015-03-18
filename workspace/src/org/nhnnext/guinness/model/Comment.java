package org.nhnnext.guinness.model;

public class Comment {
	private long commentId;
	private String commentText;
	private char commnetType;
	private String userId;
	private String noteId;
	//createDate
	
	public Comment(long commentId, String commentText, char commnetType,
			String userId, String noteId) {
		this.commentId = commentId;
		this.commentText = commentText;
		this.commnetType = commnetType;
		this.userId = userId;
		this.noteId = noteId;
	}
	
	
}
