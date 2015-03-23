package org.nhnnext.guinness.model;


public class Note {
	//MUST HAVE
	private String noteId;
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;
	
	//NOT MUST
	private String userName;
	
	public Note() {
		this.noteText = null;
		this.targetDate = null;
		this.userId = null;
		this.groupId = null;
	}

	public Note(String noteText, String targetDate, String userId, String groupId) {
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
	}

	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
	}
	
	public Note(String noteId, String noteText, String targetDate, String userId,
			String groupId, String userName) {
		this.noteId = noteId;
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
	
	public String getNoteId() {
		return noteId;
	}
	
	public String getUserName() {
		return userName;
	}
}
