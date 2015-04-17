package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.dao.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
public class ReadGroupController {
	private static final Logger logger = LoggerFactory.getLogger(ReadGroupController.class);

	@Autowired
	private GroupDao groupDao;
	
	@RequestMapping("/group/read")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		List<Group> groupList = null;
		try {
			groupList = groupDao.readGroupList(sessionUserId);
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
			return;
		}
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(groupList));
		out.close();
	}
}
