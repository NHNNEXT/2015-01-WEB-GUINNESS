package org.nhnnext.guinness.model;

public class Note {
	private long noteId;
	private String noteText;
	private String targetDate;
	private String userId;
	private String groupId;
	//createDate?
	
	public Note(long noteId, String noteText, String targetDate, String userId,
			String groupId) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.targetDate = targetDate;
		this.userId = userId;
		this.groupId = groupId;
	}
	
	
}
