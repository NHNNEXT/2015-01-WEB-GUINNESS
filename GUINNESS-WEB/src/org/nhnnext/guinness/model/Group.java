package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.Random;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class Group {
	@Size(min = 5, max = 5)
	private String groupId;

	@Size(min = 1, max = 50)
	private String groupName;

	@Email
	@Size(min = 1, max = 50)
	private String groupCaptainUserId;

	@NotNull
	private char isPublic;

	public Group(String groupId, String groupName, String groupCaptainUserId, char isPublic) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCaptainUserId = groupCaptainUserId;
		this.isPublic = isPublic;
	}

	public Group(String groupName, String groupCaptainUserId, char isPublic) throws MakingObjectListFromJdbcException,
			SQLException {
		this(setNewGroupId(), groupName, groupCaptainUserId, isPublic);
	}

	public static String setNewGroupId() throws MakingObjectListFromJdbcException, SQLException {
		//TODO CodeReview : while vs 재귀
		while (true) {
			String groupId = getRandomId(ParameterKey.lengthOfGroupId);
			if (GroupDao.getInstance().readGroup(groupId) == null) {
				return groupId;
			}
		}
	}

	private static String getRandomId(int lengthOfReturnString) {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		// TODO http://www.slipp.net/questions/361 문서 참고
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < lengthOfReturnString; i++) {
			buf.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
		return buf.toString();
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

	public char isPublic() {
		return isPublic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupCaptainUserId == null) ? 0 : groupCaptainUserId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + isPublic;
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
		if (isPublic != other.isPublic)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", groupCaptainUserId=" + groupCaptainUserId
				+ ", isPublic=" + isPublic + "]";
	}
}
