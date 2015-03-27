package org.nhnnext.guinness.model;

public class Note {
	// MUST HAVE
	private String noteId;
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;

	// NOT MUST
	private String userName;

	public Note(String noteText, String targetDate, String userId, String groupId) {
		this(null, noteText, targetDate, userId, groupId, null);
	}

	public Note(String noteId, String noteText, String targetDate, String userId, String groupId, String userName) {
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
