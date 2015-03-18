package org.nhnnext.guinness.model;

public class Group {
	private String groupId;
	private String groupName;
	private String groupCaptainUserId;
	private boolean isPublic;
	// createDate
	
	public Group(String groupId, String groupName, String groupCaptainUserId,
			boolean isPublic) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCaptainUserId = groupCaptainUserId;
		this.isPublic = isPublic;
	}
	
	
}
