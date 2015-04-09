package org.nhnnext.guinness.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ServletRequestUtil {

	public static String checkSessionAttribute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("sessionUserId");
		if(userId == null){
			resp.sendRedirect("/");
			return null;
		}
		return userId;
	}

	public static Map<String, String> getRequestParameters(HttpServletRequest req, String... parameters) {
		Map<String, String> map = new HashMap<String, String>();
		for (String param : parameters) {
			map.put(param, req.getParameter(param));
		}
		return map;
	}
}
