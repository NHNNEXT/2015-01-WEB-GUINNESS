package org.nhnnext.guinness.controller.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.WebServletURL;

@WebServlet(WebServletURL.USER_LOGOUT)
public class LogoutUserServlet extends HttpServlet{
	private static final long serialVersionUID = -6472067457011797683L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("sessionUserId");
		session.removeAttribute("sessionUserName");
		resp.sendRedirect("/");
	}
}
