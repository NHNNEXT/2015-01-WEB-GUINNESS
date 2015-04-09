package org.nhnnext.guinness.common;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO http://www.slipp.net/questions/360 글 참고해 수정
public class Forwarding {
	public static void doForward(HttpServletRequest req, HttpServletResponse resp, String errorKey,
			String errorMessage, String destination) throws ServletException, IOException {
		req.setAttribute(errorKey, errorMessage);
		RequestDispatcher rd = req.getRequestDispatcher(destination);
		resp.setStatus(500);
		rd.forward(req, resp);
	}

	public static void doForward(HttpServletRequest req, HttpServletResponse resp, String destination)
			throws ServletException, IOException {
		doForward(req, resp, "", "", destination);
	}

	public static void forwardForException(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		doForward(req, resp, "", "", "/exception.jsp");
	}
}
