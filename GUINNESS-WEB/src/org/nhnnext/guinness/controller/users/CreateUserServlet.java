package org.nhnnext.guinness.controller.users;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(WebServletUrl.USER_CREATE)
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CreateUserServlet.class);
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException,
			java.io.IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword", "userName");
		User user = new User(paramsList.get("userId"), paramsList.get("userName"), paramsList.get("userPassword"));

		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (constraintViolations.size() > 0) {
			String signValidErrorMessage = "";
			Iterator<ConstraintViolation<User>> violations = constraintViolations.iterator();
			while (violations.hasNext()) {
				ConstraintViolation<User> each = violations.next();
				signValidErrorMessage = signValidErrorMessage + "<br />" + each.getMessage();
			}
			req.setAttribute("userId", paramsList.get("userId"));
			req.setAttribute("userName", paramsList.get("userName"));
			Forwarding.doForward(req, resp, "signValidErrorMessage", signValidErrorMessage, "/");
			return;
		}

		try {
			if (!UserDao.getInstance().createUser(user)) {
				Forwarding.doForward(req, resp, "message", "이미 존재하는 아이디입니다.", "/");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", paramsList.get("userId"));
			session.setAttribute("sessionUserName", paramsList.get("userName"));
			resp.sendRedirect("/groups.jsp");
		} catch (SQLException | ClassNotFoundException e) {
			logger.error(e.getClass().getSimpleName() + "에서 exception 발생", e);
			resp.sendRedirect("/exception.jsp");
		}
	}
}
