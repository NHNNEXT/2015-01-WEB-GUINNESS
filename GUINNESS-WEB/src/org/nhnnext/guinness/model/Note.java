package org.nhnnext.guinness.model;

public class Note {
	private String noteId;
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;
	private String userName;
	private int commentCount;
	private String groupName;

	public Note(String noteText, String targetDate, String userId, String groupId) {
		this(null, noteText, targetDate, userId, groupId, null, 0);
	}

	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName) {
		this(noteId, noteText, targetDate, userId, groupId, userName, 0);
	}
	
	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName, int commentCount) {
		this(noteId, noteText, targetDate, userId, groupId, userName, commentCount, null);
	}
	
	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName, int commentCount, String groupName) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
		this.userName = userName;
		this.commentCount = commentCount;
		this.groupName = groupName;
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
	
	public String getGroupName() {
		return groupName;
	}

	@Override
    public String toString() {
	    return "Note [noteId=" + noteId + ", noteText=" + noteText + ", targetDate=" + targetDate + ", userId="
	            + userId + ", groupId=" + groupId + ", userName=" + userName + "]";
    }
}
