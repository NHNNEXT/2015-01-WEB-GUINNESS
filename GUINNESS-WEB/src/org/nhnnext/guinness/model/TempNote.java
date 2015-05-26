package org.nhnnext.guinness.model;

public class TempNote {
	private String noteId;
	private String noteText;
	private String createDate;
	private User user;
	
	public TempNote(String noteId, String noteText, String createDate, User user) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.createDate = createDate;
		this.user = user;
	}

	public TempNote(String noteText, User user) {
		this.noteText = noteText;
		this.user = user;
	}

	public String getNoteId() {
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

	public void setNoteId(String noteId) {
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
