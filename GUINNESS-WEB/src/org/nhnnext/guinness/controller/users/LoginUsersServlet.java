package org.nhnnext.guinness.controller.users;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;

@WebServlet(WebServletUrl.USER_LOGIN)
public class LoginUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException,
			java.io.IOException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		PrintWriter out = resp.getWriter();
		try {
			User user = UserDao.getInstance().readUser(userId);
			if (user == null || !user.getUserPassword().equals(userPassword)) {
				out.print("loginFailed");
				out.close();
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", user.getUserId());
			session.setAttribute("sessionUserName", user.getUserName());
			out.print("/groups.jsp");
		} catch (SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			out.print("/exception.jsp");
		}
		out.close();
	}
}
