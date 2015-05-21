package org.nhnnext.guinness.model;


public class Note {
	private String noteId;
	private String noteText;
	private String noteTargetDate;
	private User user;
	private Group group;
	private int commentCount;
	
	public Note(String noteId, String noteText, String noteTargetDate, User user, Group group, int commentCount) {
		this.noteId = noteId;
		this.noteText = noteText;
		this.noteTargetDate = noteTargetDate;
		this.user = user;
		this.group = group;
		this.commentCount = commentCount;
	}

	public Note(String noteText, String noteTargetDate, User user, Group group) {
		super();
		this.noteText = noteText;
		this.noteTargetDate = noteTargetDate;
		this.user = user;
		this.group = group;
	}

	public Note(String noteId) {
		this(noteId, null, null, null, null, 0);
	}
	
	public Note(String noteId, String noteTargetDate, int commentCount) {
		this(noteId, null, noteTargetDate, null, null, commentCount);
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public String getNoteTargetDate() {
		return noteTargetDate;
	}

	public void setNoteTargetDate(String noteTargetDate) {
		this.noteTargetDate = noteTargetDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + commentCount;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((noteId == null) ? 0 : noteId.hashCode());
		result = prime * result + ((noteText == null) ? 0 : noteText.hashCode());
		result = prime * result + ((noteTargetDate == null) ? 0 : noteTargetDate.hashCode());
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
		Note other = (Note) obj;
		if (commentCount != other.commentCount)
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
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
		if (noteTargetDate == null) {
			if (other.noteTargetDate != null)
				return false;
		} else if (!noteTargetDate.equals(other.noteTargetDate))
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
		return "Note [noteId=" + noteId + ", noteText=" + noteText + ", noteTargetDate=" + noteTargetDate + ", user=" + user
				+ ", group=" + group + ", commentCount=" + commentCount + "]";
	}
}