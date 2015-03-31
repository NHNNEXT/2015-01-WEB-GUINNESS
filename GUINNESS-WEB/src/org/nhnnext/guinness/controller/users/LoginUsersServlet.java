package org.nhnnext.guinness.controller.users;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;

@WebServlet(WebServletURL.USER_LOGIN)
public class LoginUsersServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		UserDao userDao = new UserDao();
		User user = null;
		PrintWriter out = resp.getWriter();
		try {
			user = userDao.checkLogin(userId, userPassword);
			if (user == null) {
				out.print("loginFailed");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", user.getUserId());
			session.setAttribute("sessionUserName", user.getUserPassword());
			out.print("/groups.jsp");
			
		} catch (SQLException e) {
			out.print("/exception.jsp");
		}
	}
}
