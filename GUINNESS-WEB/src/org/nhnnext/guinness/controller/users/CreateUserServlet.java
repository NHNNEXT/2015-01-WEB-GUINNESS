package org.nhnnext.guinness.controller.users;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.nhnnext.guinness.exception.AlreadyExistedUserIdException;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.UserDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.MyValidatorFactory;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/user/create")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CreateUserServlet.class);

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException,
			java.io.IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword",
				"userName");
		User user = new User(paramsList.get("userId"), paramsList.get("userName"), paramsList.get("userPassword"));

		Set<ConstraintViolation<User>> constraintViolations = MyValidatorFactory.createValidator().validate(user);
		if (!constraintViolations.isEmpty()) {
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
			UserDao.getInstance().createUser(user);
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", paramsList.get("userId"));
			session.setAttribute("sessionUserName", paramsList.get("userName"));
			resp.sendRedirect("/groups.jsp");
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			resp.sendRedirect("/exception.jsp");
		} catch (AlreadyExistedUserIdException e) {
			logger.error("Exception", e);
			Forwarding.doForward(req, resp, "message", "이미 존재하는 아이디입니다.", "/");
		}
	}
}
