package org.nhnnext.guinness.controller.users;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.User;

@WebServlet("/users/create")
public class CreateUserServlet extends HttpServlet{
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException ,java.io.IOException {
		String userId = (String) req.getAttribute("userId");
		String userPassword = (String) req.getAttribute("userPassword");
		String userName = (String) req.getAttribute("userName");
		
		User user = new User(userId, userName, userPassword);
		
		
		
	};
	
	
}
