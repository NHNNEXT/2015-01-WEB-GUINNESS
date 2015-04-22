package org.nhnnext.guinness.model;

public class Comment {
	private String commentText;
	private String commentType;
	private String createDate;
	private String userId;
	private String noteId;
	private String userName;
	private String commentId;

	public Comment(String commentText, String commentType, String createDate, String userId, String noteId,
			String userName, String commentId) {
		this.commentText = commentText;
		this.commentType = commentType;
		this.createDate = createDate;
		this.userId = userId;
		this.noteId = noteId;
		this.userName = userName;
		this.commentId = commentId;
	}

	public Comment(String commentText, String commentType, String createDate, String userId, String noteId,
			String userName) {
		this(commentText, commentType, createDate, userId, noteId, userName, null);
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

	public String getCommentId() {
		return commentId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
		result = prime * result + ((commentType == null) ? 0 : commentType.hashCode());
		result = prime * result + ((noteId == null) ? 0 : noteId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (commentText == null) {
			if (other.commentText != null)
				return false;
		} else if (!commentText.equals(other.commentText))
			return false;
		if (commentType == null) {
			if (other.commentType != null)
				return false;
		} else if (!commentType.equals(other.commentType))
			return false;
		if (noteId == null) {
			if (other.noteId != null)
				return false;
		} else if (!noteId.equals(other.noteId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Comment [commentText=" + commentText + ", commentType=" + commentType + ", userId=" + userId
				+ ", noteId=" + noteId + "]";
	}

}
