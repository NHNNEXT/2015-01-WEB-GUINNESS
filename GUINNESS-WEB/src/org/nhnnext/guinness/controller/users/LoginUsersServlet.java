package org.nhnnext.guinness.controller.users;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/login")
public class LoginUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LoginUsersServlet.class);
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException,
			java.io.IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword");
		PrintWriter out = resp.getWriter();
		try {
			User user = UserDao.getInstance().readUser(paramsList.get("userId"));
			if (user == null || !user.getUserPassword().equals(paramsList.get("userPassword"))) {
				out.print("loginFailed");
				out.close();
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", user.getUserId());
			session.setAttribute("sessionUserName", user.getUserName());
			out.print("/groups.jsp");
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("Exception", e);
			out.print("/exception.jsp");
		}
		out.close();
	}
}
