package org.nhnnext.guinness.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.google.gson.annotations.Expose;

public class User {
	@NotNull
	@Email(message = "이메일 주소가 유효하지 않습니다.")
	@Size(max = 50, message = "이메일은 50 글자 이하만 사용 가능합니다.")
	@Expose
	private String userId;
	
	@NotNull
	@Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)", message = "이름은 한글, 영문, 숫자만 가능합니다.")
	@Size(max = 50, message = "이름은 25 글자 이하만 사용 가능합니다.")
	@Expose
	private String userName;
	
	@NotNull
	@Pattern(regexp = "([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z])", message = "비밀번호는 영어 대소문자와 숫자를 포함해야합니다.")
	@Size(min = 8, max = 16, message = "비밀번호는 8자리 이상, 16자리 이하로 사용해야합니다.")
	@Expose(serialize=false)
	private String userPassword;
	
	private String status;
	
	@Expose
	private String userImage;
	
	
	 public User() {
	}

	public User(String userId, String userName, String userPassword, String status, String userImage) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.status = status;
		this.userImage = userImage;
	}
	
	public User(String userId, String userName, String userPassword, String status) {
		this(userId, userName, userPassword, status, "avatar-default.png");
	}

	public User(String userId) {
		this(userId, null, null, null, "avatar-default.png");
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userImage == null) ? 0 : userImage.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((userPassword == null) ? 0 : userPassword.hashCode());
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
		User other = (User) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userImage == null) {
			if (other.userImage != null)
				return false;
		} else if (!userImage.equals(other.userImage))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userPassword == null) {
			if (other.userPassword != null)
				return false;
		} else if (!userPassword.equals(other.userPassword))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword + ", status="
				+ status + ", userImage=" + userImage + "]";
	}
}