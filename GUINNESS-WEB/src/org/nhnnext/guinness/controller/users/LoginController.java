package org.nhnnext.guinness.controller.users;

import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.dao.UserDao;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "userId", "userPassword");
		PrintWriter out = resp.getWriter();
		try {
			User user = userDao.readUser(paramsList.get("userId"));
			if (user == null || !user.getUserPassword().equals(paramsList.get("userPassword"))) {
				out.print("loginFailed");
				out.close();
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("sessionUserId", user.getUserId());
			session.setAttribute("sessionUserName", user.getUserName());
			out.print("/groups.jsp");
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			out.print("/exception.jsp");
		}
		out.close();
	}
}
