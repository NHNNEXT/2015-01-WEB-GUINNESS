package org.nhnnext.guinness.util;

import java.io.IOException;

import javax.servlet.http.HttpSession;

public class ServletRequestUtil {
	private ServletRequestUtil() {
	}
	
	public static boolean existedUserIdFromSession(HttpSession session) throws IOException {
		if (session.getAttribute("sessionUserId") == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public static String getUserIdFromSession(HttpSession session) throws IOException {
		if(!existedUserIdFromSession(session)){
			return null;
		}
		return (String) session.getAttribute("sessionUserId");
	}
}
