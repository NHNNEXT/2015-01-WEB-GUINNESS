package org.nhnnext.guinness.model;

public class Note {
	private String noteId;
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;
	private String userName;
	private int commentCount;
	private String userImage;

	public Note(String noteText, String targetDate, String userId, String groupId) {
		this(null, noteText, targetDate, userId, groupId, null, 0, null);
	}

	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName) {
		this(noteId, noteText, targetDate, userId, groupId, userName, 0, null);
	}
	
	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName, int commentCount) {
		this(noteId, noteText, targetDate, userId, groupId, userName, commentCount, null);
	}
	
	
	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName, int commentCount, String userImage) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
		this.userName = userName;
		this.commentCount = commentCount;
		this.userImage = userImage;
	}
	
	public String getNoteId() {
		return noteId;
	}

	public String getNoteText() {
		return noteText;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public String getUserId() {
		return userId;
	}

	public String getGroupId() {
		return groupId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public int getCommentCount() {
		return commentCount;
	}

	public String getUserImage() {
		return userImage;
	}
	
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + commentCount;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((noteId == null) ? 0 : noteId.hashCode());
		result = prime * result + ((noteText == null) ? 0 : noteText.hashCode());
		result = prime * result + ((targetDate == null) ? 0 : targetDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userImage == null) ? 0 : userImage.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		Note other = (Note) obj;
		if (commentCount != other.commentCount)
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (noteId == null) {
			if (other.noteId != null)
				return false;
		} else if (!noteId.equals(other.noteId))
			return false;
		if (noteText == null) {
			if (other.noteText != null)
				return false;
		} else if (!noteText.equals(other.noteText))
			return false;
		if (targetDate == null) {
			if (other.targetDate != null)
				return false;
		} else if (!targetDate.equals(other.targetDate))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userImage == null) {
			if (other.userImage != null)
				return false;
		} else if (!userImage.equals(other.userImage))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", noteText=" + noteText + ", targetDate=" + targetDate + ", userId="
				+ userId + ", groupId=" + groupId + ", userName=" + userName + ", commentCount=" + commentCount
				+ ", userImage=" + userImage + "]";
	}

}
