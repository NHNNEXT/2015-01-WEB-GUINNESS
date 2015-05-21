package org.nhnnext.guinness.model;

import javax.validation.constraints.Size;

public class Group {
	private String groupId;
	@Size(min = 1, max = 50)
	private String groupName;
	private String groupCaptainUserId;
	private String status;
	private String groupImage;
	
	public Group() {
	}

	public Group(String groupId, String groupName, String groupCaptainUserId, String status) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCaptainUserId = groupCaptainUserId;
		this.status = status;
	}

	public Group(String groupId) {
		this(groupId, null, null, "A");
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupCaptainUserId() {
		return groupCaptainUserId;
	}

	public String getStatus() {
		return status;
	}
	
	public String getGroupImage() {
		return groupImage;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupCaptainUserId(String groupCaptainUserId) {
		this.groupCaptainUserId = groupCaptainUserId;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setGroupImage(String groupImage) {
		this.groupImage = groupImage;
	}

	public boolean checkStatus() {
		return status.equals("T");
	}
	
	public boolean checkCaptain(String userId) {
		return this.groupCaptainUserId.equals(userId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupCaptainUserId == null) ? 0 : groupCaptainUserId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Group other = (Group) obj;
		if (groupCaptainUserId == null) {
			if (other.groupCaptainUserId != null)
				return false;
		} else if (!groupCaptainUserId.equals(other.groupCaptainUserId))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", groupCaptainUserId=" + groupCaptainUserId
				+ ", isPublic=" + status + "]";
	}
}
