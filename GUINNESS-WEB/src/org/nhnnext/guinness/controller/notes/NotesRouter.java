package org.nhnnext.guinness.controller.notes;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotesRouter {
	@Autowired
	private GroupDao groupDao;

	@RequestMapping(value = "/g/*")
	protected void excute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		String url = req.getRequestURI().split("/")[2];
		
		if (!groupDao.checkJoinedGroup(sessionUserId, url)) {
			Forwarding.doForward(req, resp, "errorMessage", "비정상적 접근시도.", "/illegal.jsp");
			return;
		}
		Forwarding.doForward(req, resp, "/notes.jsp");
	}
}
