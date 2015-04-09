package org.nhnnext.guinness.common;

// TODO http://www.slipp.net/questions/360 글 참고해 수정
// TODO http://www.slipp.net/questions/358 글도 참고
public class WebServletUrl {
	public static final String GROUP_CREATE = "/group/create";
	public static final String GROUP_DELETE = "/group/delete";
	public static final String GROUP_READ = "/group/read";
	public static final String GROUP_ADD_MEMBER = "/group/add/member";
	public static final String GROUP_READ_MEMBER = "/group/read/member";

	public static final String NOTELIST = "/g/*";
	public static final String NOTELIST_READ = "/notelist/read";
	public static final String NOTELIST_UPDATE = "/notelist/update";
	public static final String NOTE_CREATE = "/note/create";
	public static final String NOTE_READ = "/note/read";

	public static final String USER_CREATE = "/user/create";
	public static final String USER_LOGIN = "/user/login";
	public static final String USER_LOGOUT = "/user/logout";

	public static final String COMMENT_CREATE = "/comment/create";
	public static final String COMMENT_READ = "/comment/read";
}
