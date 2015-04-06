package org.nhnnext.guinness.controller.users;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringEscapeUtils;
import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.MyValidatorFactory;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;


@WebServlet(WebServletURL.USER_CREATE)
public class CreateUserServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException, java.io.IOException {
		String userId = req.getParameter("userId");
		String userPassword = req.getParameter("userPassword");
		String userName =  StringEscapeUtils.escapeHtml4(req.getParameter("userName"));
		
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
			Forwarding.forwardForError(req, resp, "signValidErrorMessage", signValidErrorMessage, "/");
			return;
		}
		
		UserDao userDao = new UserDao();
		try {
			if (!userDao.createUser(user)) {
				Forwarding.forwardForError(req, resp, "message", "이미 존재하는 아이디입니다.", "/");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", userId);
			session.setAttribute("sessionUserName", userName);
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendRedirect("/exception.jsp");
		}
	}
}
