package org.nhnnext.guinness.model;


public class Note {
	private long noteId;
	private String noteText;

	private String targetDate;
	private String userId;
	private String groupId;
	//createDate?
	
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
	
}
