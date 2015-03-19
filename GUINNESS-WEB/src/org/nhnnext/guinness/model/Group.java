package org.nhnnext.guinness.model;

public class Group {
	private String groupId;
	private String groupName;
	private String groupCaptainUserId;
	private int isPublic;
	private String createDate;
	// createDate
	
	public Group(String groupId, String groupName, String groupCaptainUserId, String createDate,
			int isPublic) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCaptainUserId = groupCaptainUserId;
		this.createDate=createDate;
		this.isPublic = isPublic;
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

	public int isPublic() {
		return isPublic;
	}

	public String getCreateDate() {
		return createDate;
	}
	
	
	
	
}
