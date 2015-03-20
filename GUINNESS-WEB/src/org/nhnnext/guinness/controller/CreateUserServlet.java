package org.nhnnext.guinness.controller;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			userDao.createUser(user);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	};
	
	
}
