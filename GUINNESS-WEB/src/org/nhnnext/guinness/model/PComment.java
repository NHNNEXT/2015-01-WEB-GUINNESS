package org.nhnnext.guinness.model;

public class PComment {
	private String pCommentId;
	private String pId;
	private String sameSenCount;
	private String sameSenIndex;
	private String pCommentText;
	private String selectedText;
	private String pCommentCreateDate;
	private SessionUser sessionUser;
	private Note note;

	public PComment(String pCommentId, String pId, String sameSenCount, String sameSenIndex, String pCommentText,
			String selectedText, String pCommentCreateDate, SessionUser sessionUser, Note note) {
		this.pCommentId = pCommentId;
		this.pId = pId;
		this.sameSenCount = sameSenCount;
		this.sameSenIndex = sameSenIndex;
		this.pCommentText = pCommentText;
		this.selectedText = selectedText;
		this.pCommentCreateDate = pCommentCreateDate;
		this.sessionUser = sessionUser;
		this.note = note;
	}
	
	
	public PComment(String pId, String sameSenCount, String sameSenIndex, String pCommentText,
			String selectedText, SessionUser sessionUser, Note note) {
		this(null, pId, sameSenCount, sameSenIndex, pCommentText, selectedText, null, sessionUser, note);
	}
	
//	public PComment(String pId, String pCommentText, String selectedText, SessionUser sessionUser, Note note) {
//		this(null, pId, null, null, pCommentText, selectedText, null, sessionUser, note);
//	}

	public String getpCommentId() {
		return pCommentId;
	}

	public String getpId() {
		return pId;
	}

	public String getSameSenCount() {
		return sameSenCount;
	}

	public String getSameSenIndex() {
		return sameSenIndex;
	}

	public String getpCommentText() {
		return pCommentText;
	}

	public String getSelectedText() {
		return selectedText;
	}

	public String getpCommentCreateDate() {
		return pCommentCreateDate;
	}

	public SessionUser getSessionUser() {
		return sessionUser;
	}

	public Note getNote() {
		return note;
	}

	public void setpCommentId(String pCommentId) {
		this.pCommentId = pCommentId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public void setSameSenCount(String sameSenCount) {
		this.sameSenCount = sameSenCount;
	}

	public void setSameSenIndex(String sameSenIndex) {
		this.sameSenIndex = sameSenIndex;
	}

	public void setpCommentText(String pCommentText) {
		this.pCommentText = pCommentText;
	}

	public void setSelectedText(String selectedText) {
		this.selectedText = selectedText;
	}

	public void setpCommentCreateDate(String pCommentCreateDate) {
		this.pCommentCreateDate = pCommentCreateDate;
	}

	public void setSessionUser(SessionUser sessionUser) {
		this.sessionUser = sessionUser;
	}

	public void setNote(Note note) {
		this.note = note;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((pCommentCreateDate == null) ? 0 : pCommentCreateDate.hashCode());
		result = prime * result + ((pCommentId == null) ? 0 : pCommentId.hashCode());
		result = prime * result + ((pCommentText == null) ? 0 : pCommentText.hashCode());
		result = prime * result + ((pId == null) ? 0 : pId.hashCode());
		result = prime * result + ((sameSenCount == null) ? 0 : sameSenCount.hashCode());
		result = prime * result + ((sameSenIndex == null) ? 0 : sameSenIndex.hashCode());
		result = prime * result + ((selectedText == null) ? 0 : selectedText.hashCode());
		result = prime * result + ((sessionUser == null) ? 0 : sessionUser.hashCode());
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
		PComment other = (PComment) obj;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (pCommentCreateDate == null) {
			if (other.pCommentCreateDate != null)
				return false;
		} else if (!pCommentCreateDate.equals(other.pCommentCreateDate))
			return false;
		if (pCommentId == null) {
			if (other.pCommentId != null)
				return false;
		} else if (!pCommentId.equals(other.pCommentId))
			return false;
		if (pCommentText == null) {
			if (other.pCommentText != null)
				return false;
		} else if (!pCommentText.equals(other.pCommentText))
			return false;
		if (pId == null) {
			if (other.pId != null)
				return false;
		} else if (!pId.equals(other.pId))
			return false;
		if (sameSenCount == null) {
			if (other.sameSenCount != null)
				return false;
		} else if (!sameSenCount.equals(other.sameSenCount))
			return false;
		if (sameSenIndex == null) {
			if (other.sameSenIndex != null)
				return false;
		} else if (!sameSenIndex.equals(other.sameSenIndex))
			return false;
		if (selectedText == null) {
			if (other.selectedText != null)
				return false;
		} else if (!selectedText.equals(other.selectedText))
			return false;
		if (sessionUser == null) {
			if (other.sessionUser != null)
				return false;
		} else if (!sessionUser.equals(other.sessionUser))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "PComment [pCommentId=" + pCommentId + ", pId=" + pId + ", sameSenCount=" + sameSenCount
				+ ", sameSenIndex=" + sameSenIndex + ", pCommentText=" + pCommentText + ", selectedText="
				+ selectedText + ", pCommentCreateDate=" + pCommentCreateDate + ", sessionUser=" + sessionUser
				+ ", note=" + note + "]";
	}
}
