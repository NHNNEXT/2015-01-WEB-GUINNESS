package org.nhnnext.guinness.controller.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.common.MyValidatorFactory;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;


@WebServlet(WebServletURL.USER_CREATE)
public class CreateUserServlet extends HttpServlet{
	private static final long serialVersionUID = -8433534495044878880L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
		req.setCharacterEncoding("utf-8");
		String userId = (String) req.getParameter("userId");
		String userPassword = (String) req.getParameter("userPassword");
		String userName = (String) req.getParameter("userName");

		User user = new User(userId, userName, userPassword);
		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (constraintViolations.size() > 0) {
			String signValidErrorMessage = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				signValidErrorMessage = signValidErrorMessage + "<br />" + each.getMessage();
			}
			req.setAttribute("userId", userId);
			req.setAttribute("userName", userName);
			forwardJSP(req, resp, signValidErrorMessage);
			return;
		}
		UserDao userDao = new UserDao();
		try {
			if (userId == null) {
				resp.sendRedirect("/");
			}
			if (userDao.createUser(user)) {
				HttpSession session = req.getSession();
				session.setAttribute("sessionUserId", userId);
				session.setAttribute("sessionUserName", userName);
				resp.sendRedirect("/groups.jsp");
				return;
			}
			req.setAttribute("message", "이미 존재하는 아이디입니다.");
			RequestDispatcher rd = req.getRequestDispatcher("/");
			rd.forward(req, resp);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			resp.sendRedirect("/exception.jsp");
		}
	}

	private void forwardJSP(HttpServletRequest req, HttpServletResponse resp, Object signValidErrorMessage) throws ServletException, IOException {
		req.setAttribute("signValidErrorMessage", signValidErrorMessage);
		RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
		rd.forward(req, resp);
	};
}
