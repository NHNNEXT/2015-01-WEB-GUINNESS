package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotesRouter {
	@Autowired
	private GroupDao groupDao;

	@RequestMapping(value = "/g/{url}")
	protected String excute(@PathVariable String url, HttpSession session, Model model) throws IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			return "redirect:/";
		}
		
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		
		if (!groupDao.checkJoinedGroup(sessionUserId, url)) {
			model.addAttribute("errorMessage", "비정상적 접근시도.");
			return "illegal";
		}
		
		return "notes";
	}
}
