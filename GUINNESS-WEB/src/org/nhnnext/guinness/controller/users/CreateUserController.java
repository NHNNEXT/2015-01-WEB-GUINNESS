package org.nhnnext.guinness.controller.users;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateUserController {
	private static final Logger logger = LoggerFactory.getLogger(CreateUserController.class);
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	protected String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
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
			return null;
		}

		try {
			userDao.createUser(user);
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", paramsList.get("userId"));
			session.setAttribute("sessionUserName", paramsList.get("userName"));
			return "groups";
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			return "exception";
		} catch (AlreadyExistedUserIdException e) {
			logger.error("Exception", e);
			Forwarding.doForward(req, resp, "message", "이미 존재하는 아이디입니다.", "/");
		}
		return null;
	}
}
