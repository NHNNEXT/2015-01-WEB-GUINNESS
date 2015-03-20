package org.nhnnext.guinness.controller.users;

import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDAO;

@WebServlet("/users/create")
public class CreateUserServlet extends HttpServlet{
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException ,java.io.IOException {
		String userId = (String) req.getParameter("userId");
		String userPassword = (String) req.getParameter("userPassword");
		String userName = (String) req.getParameter("userName");
		
		User user = new User(userId, userName, userPassword);
		
		UserDAO userDao = new UserDAO();
		
		try {
			if (userId == null) {
				resp.sendRedirect("/");
			}
			if (userDao.createUser(user)) {
				HttpSession session = req.getSession();
				session.setAttribute("SessionUserId", userId);
				resp.sendRedirect("/groups.jsp");
				return;
			}
			req.setAttribute("message", "이미 존재하는 아이디입니다.");
			RequestDispatcher rd = req.getRequestDispatcher("/");
			rd.forward(req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	
}
