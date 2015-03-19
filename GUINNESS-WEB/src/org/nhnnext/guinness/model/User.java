package org.nhnnext.guinness.model;

public class User {
	private String userId;
	private String userName;
	private String userPassword;
	private byte userImage;
//	private String createDate;
	public User() {}
	
	public User(String userId, String userName, String userPassword,
			byte userImage) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userImage = userImage;
	}
	
	public User(String userId, String userName, String userPassword) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public byte getUserImage() {
		return userImage;
	}
	
}
