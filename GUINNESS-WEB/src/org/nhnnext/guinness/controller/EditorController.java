package org.nhnnext.guinness.controller;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.GroupDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.google.gson.Gson;

@Controller
public class EditorController {
	private static final Logger logger = LoggerFactory.getLogger(EditorController.class);
	
	@Autowired
	private GroupDao groupDao;
	
	@RequestMapping(value="/note/editor", method=RequestMethod.GET)
	private String Editor (WebRequest req, HttpSession session, Model model)  {
		String groupId = req.getParameter("groupId");
		String groupName = groupDao.readGroup(groupId).getGroupName();
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupName", new Gson().toJson(groupName));
		return "editor";
	}
}
