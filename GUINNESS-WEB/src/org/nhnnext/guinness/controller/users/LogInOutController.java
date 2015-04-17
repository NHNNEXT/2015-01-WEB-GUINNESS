package org.nhnnext.guinness.controller.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.User;
import org.nhnnext.guinness.model.dao.UserDao;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogInOutController {
	private static final Logger logger = LoggerFactory.getLogger(LogInOutController.class);

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
			out.print("/groups");
		} catch (ClassNotFoundException e) {
			logger.error("Exception", e);
			out.print("/exception.jsp");
		}
		out.close();
	}
	
	@RequestMapping("/user/logout")
	protected void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		session.invalidate();
		resp.sendRedirect("/");
	}
}
