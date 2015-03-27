package org.nhnnext.guinness.model;


public class Note {
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;
	
	//NOT MUST
	private String userName;
	
	public Note(String noteText, String targetDate, String userId, String groupId) {
		this(noteText, targetDate, userId, groupId, null);
	}
	
	public Note(String noteText, String targetDate, String userId,
			String groupId, String userName) {
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
		this.userName = userName;
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

	@Override
	public String toString() {
		return "Note [noteText=" + noteText + ", targetDate=" + targetDate
				+ ", userId=" + userId + ", groupId=" + groupId + ", userName="
				+ userName + "]";
	}
}
