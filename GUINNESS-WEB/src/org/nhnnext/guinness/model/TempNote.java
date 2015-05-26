package org.nhnnext.guinness.model;

public class TempNote {
	private long noteId;
	private String noteText;
	private String createDate;
	private User user;
	
	public TempNote(long noteId, String noteText, String createDate, User user) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.createDate = createDate;
		this.user = user;
	}

	public TempNote(String noteText, String createDate, User user) {
		this(0, noteText, createDate, user);
	}

	public long getNoteId() {
		return noteId;
	}

	public String getNoteText() {
		return noteText;
	}

	public String getCreateDate() {
		return createDate;
	}

	public User getUser() {
		return user;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "TempNote [noteId=" + noteId + ", noteText=" + noteText + ", createDate=" + createDate + ", user="
				+ user + "]";
	}
}
