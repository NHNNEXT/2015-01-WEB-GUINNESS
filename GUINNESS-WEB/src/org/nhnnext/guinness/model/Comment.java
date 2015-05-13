package org.nhnnext.guinness.model;

public class Comment {
	private String commentId;
	private String commentText;
	private String commentType;
	private String createDate;
	private User user;
	private Note note;
	
	public Comment(String commentId, String commentText, String commentType, String createDate, User user, Note note) {
		this.commentId = commentId;
		this.commentText = commentText;
		this.commentType = commentType;
		this.createDate = createDate;
		this.user = user;
		this.note = note;
	}

	public Comment(String commentText, String commentType, User user, Note note) {
		this(null, commentText, commentType, null, user, note);
	}

	public String getCommentId() {
		return commentId;
	}

	public String getCommentText() {
		return commentText;
	}

	public String getCommentType() {
		return commentType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public User getUser() {
		return user;
	}

	public Note getNote() {
		return note;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public boolean checkWriter(User noteWriter) {
		return this.getUser().getUserId().equals(noteWriter.getUserId());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
		result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
		result = prime * result + ((commentType == null) ? 0 : commentType.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
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
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Comment [commentText=" + commentText + ", commentType=" + commentType + ", createDate=" + createDate
				+ ", commentId=" + commentId + ", user=" + user + ", note=" + note + "]";
	}
}