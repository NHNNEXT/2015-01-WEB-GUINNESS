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

	public TempNote(long noteId, String noteText, String createDate) {
		this(noteId, noteText, createDate, null);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + (int) (noteId ^ (noteId >>> 32));
		result = prime * result + ((noteText == null) ? 0 : noteText.hashCode());
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
		TempNote other = (TempNote) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (noteId != other.noteId)
			return false;
		if (noteText == null) {
			if (other.noteText != null)
				return false;
		} else if (!noteText.equals(other.noteText))
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
		return "TempNote [noteId=" + noteId + ", noteText=" + noteText + ", createDate=" + createDate + ", user="
				+ user + "]";
	}
}
