package org.nhnnext.guinness.model;

public class Alarm {
	private String alarmId;
	private String calleeId;
	private String callerId;
	private String noteId;
	private String alarmText;
	private String createDate;
	
	public Alarm() {
	}
	public Alarm(String alarmId, String calleeId, String callerId, String noteId, String alarmText) {
		this(alarmId, calleeId, callerId, noteId, alarmText, null);
	}
	public Alarm(String alarmId, String calleeId, String callerId, String noteId, String alarmText, String createDate) {
		this.alarmId = alarmId;
		this.calleeId = calleeId;
		this.callerId = callerId;
		this.noteId = noteId;
		this.alarmText = alarmText;
		this.createDate = createDate;
	}
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getCalleeId() {
		return calleeId;
	}
	public void setCalleeId(String calleeId) {
		this.calleeId = calleeId;
	}
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getAlarmText() {
		return alarmText;
	}
	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alarmId == null) ? 0 : alarmId.hashCode());
		result = prime * result
				+ ((alarmText == null) ? 0 : alarmText.hashCode());
		result = prime * result
				+ ((calleeId == null) ? 0 : calleeId.hashCode());
		result = prime * result
				+ ((callerId == null) ? 0 : callerId.hashCode());
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((noteId == null) ? 0 : noteId.hashCode());
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
		if (alarmId == null) {
			if (other.alarmId != null)
				return false;
		} else if (!alarmId.equals(other.alarmId))
			return false;
		if (alarmText == null) {
			if (other.alarmText != null)
				return false;
		} else if (!alarmText.equals(other.alarmText))
			return false;
		if (calleeId == null) {
			if (other.calleeId != null)
				return false;
		} else if (!calleeId.equals(other.calleeId))
			return false;
		if (callerId == null) {
			if (other.callerId != null)
				return false;
		} else if (!callerId.equals(other.callerId))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (noteId == null) {
			if (other.noteId != null)
				return false;
		} else if (!noteId.equals(other.noteId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Alarm [alarmId=" + alarmId + ", calleeId=" + calleeId
				+ ", callerId=" + callerId + ", noteId=" + noteId
				+ ", alarmText=" + alarmText + ", createDate=" + createDate
				+ "]";
	}
}
