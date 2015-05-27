package org.nhnnext.guinness.model;

public class Alarm {
	private String alarmId;
	private String alarmStatus;
	private String alarmCreateDate;
	private SessionUser writer;
	private User reader;
	private Note note;
	private Comment comment;
	private Group group;

	public Alarm(String alarmId, String alarmStatus, SessionUser writer, User reader, Note note, Comment comment, Group group) {
		super();
		this.alarmId = alarmId;
		this.alarmStatus = alarmStatus;
		this.writer = writer;
		this.reader = reader;
		this.note = note;
		this.comment = comment;
		this.group = group;
	}
	
	public Alarm(String alarmId, String alarmStatus, SessionUser writer, User reader, Note note) {
		this(alarmId, alarmStatus, writer, reader, note, null, null);
	}
	
	public Alarm(String alarmId, String alarmStatus, SessionUser writer, User reader, Note note, Comment comment) {
		this(alarmId, alarmStatus, writer, reader, note, comment, null);
	}
	
	public Alarm(String alarmId, String alarmStatus, SessionUser writer, User reader, Group group) {
		this(alarmId, alarmStatus, writer, reader, null, null, group);
	}

	public String getAlarmId() {
		return alarmId;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public String getAlarmCreateDate() {
		return alarmCreateDate;
	}

	public SessionUser getWriter() {
		return writer;
	}

	public User getReader() {
		return reader;
	}

	public Note getNote() {
		return note;
	}

	public Comment getComment() {
		return comment;
	}

	public Group getGroup() {
		return group;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public void setAlarmCreateDate(String alarmCreateDate) {
		this.alarmCreateDate = alarmCreateDate;
	}

	public void setWriter(SessionUser writer) {
		this.writer = writer;
	}

	public void setReader(User reader) {
		this.reader = reader;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alarmCreateDate == null) ? 0 : alarmCreateDate.hashCode());
		result = prime * result + ((alarmId == null) ? 0 : alarmId.hashCode());
		result = prime * result + ((alarmStatus == null) ? 0 : alarmStatus.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((reader == null) ? 0 : reader.hashCode());
		result = prime * result + ((writer == null) ? 0 : writer.hashCode());
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
		Alarm other = (Alarm) obj;
		if (alarmCreateDate == null) {
			if (other.alarmCreateDate != null)
				return false;
		} else if (!alarmCreateDate.equals(other.alarmCreateDate))
			return false;
		if (alarmId == null) {
			if (other.alarmId != null)
				return false;
		} else if (!alarmId.equals(other.alarmId))
			return false;
		if (alarmStatus == null) {
			if (other.alarmStatus != null)
				return false;
		} else if (!alarmStatus.equals(other.alarmStatus))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (reader == null) {
			if (other.reader != null)
				return false;
		} else if (!reader.equals(other.reader))
			return false;
		if (writer == null) {
			if (other.writer != null)
				return false;
		} else if (!writer.equals(other.writer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Alarm [alarmId=" + alarmId + ", alarmStatus=" + alarmStatus + ", alarmCreateDate=" + alarmCreateDate
				+ ", writer=" + writer + ", reader=" + reader + ", note=" + note + ", comment=" + comment + ", group="
				+ group + "]";
	}
}