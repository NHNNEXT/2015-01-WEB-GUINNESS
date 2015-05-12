package org.nhnnext.guinness.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.nhnnext.guinness.dao.GroupDao;
import org.springframework.beans.factory.annotation.Autowired;

public class Group {

	@Size(min = 5, max = 5)
	private String groupId;

	@Size(min = 1, max = 50)
	private String groupName;

	@Email
	@Size(min = 1, max = 50)
	private String groupCaptainUserId;

	@NotNull
	private String isPublic;
	
	@Autowired
	private static GroupDao groupDao;
	
	public Group() {
	}

	public Group(String groupId, String groupName, String groupCaptainUserId, String isPublic) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCaptainUserId = groupCaptainUserId;
		this.isPublic = isPublic;
	}

	public Group(String groupName, String groupCaptainUserId, String isPublic) {
		this(null, groupName, groupCaptainUserId, isPublic);
	}

	public Group(String groupId) {
		this(groupId, null, null, null);
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

	public String getIsPublic() {
		return isPublic;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupCaptainUserId(String groupCaptainUserId) {
		this.groupCaptainUserId = groupCaptainUserId;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public static void setGroupDao(GroupDao groupDao) {
		Group.groupDao = groupDao;
	}

	public String isPublic() {
		return isPublic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupCaptainUserId == null) ? 0 : groupCaptainUserId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((isPublic == null) ? 0 : isPublic.hashCode());
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
		if (isPublic == null) {
			if (other.isPublic != null)
				return false;
		} else if (!isPublic.equals(other.isPublic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", groupCaptainUserId=" + groupCaptainUserId
				+ ", isPublic=" + isPublic + "]";
	}
}