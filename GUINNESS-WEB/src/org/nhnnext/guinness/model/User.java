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
	
	@Expose
	private byte userImage;
	
	 public User() {
	}

	public User(String userId, String userName, String userPassword, byte userImage) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userImage = userImage;
	}
	
	public User(String userId, String userName, String userPassword) {
		this(userId, userName, userPassword, (byte)0);
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userImage=" + userImage + "]";
	}
}
