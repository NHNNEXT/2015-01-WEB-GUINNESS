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
	private static final long serialVersionUID = -7135687406875475113L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		UserDao userDao = new UserDao();
		PrintWriter out = resp.getWriter();
		try {
			User user = userDao.readUser(userId);
			if (user == null || !user.getUserPassword().equals(userPassword)) {
				out.print("loginFailed");
				return;
			}
			out.print("/groups.jsp");
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", userId);
			session.setAttribute("sessionUserName", user.getUserName());
		} catch (SQLException | NullPointerException e) {
			System.out.println("login user servlet error");
			out.print("/exception.jsp");
		}
	}
}
