package org.nhnnext.guinness.controller;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.AlarmDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/alarm")
public class AlarmController {
	private static final Logger logger = LoggerFactory
			.getLogger(AlarmController.class);

	@Autowired
	private AlarmDao alarmDao;

	@RequestMapping("")
	protected ModelAndView list(HttpSession session) {
		return new ModelAndView("jsonView").addObject("jsonData",
				alarmDao.list((String) session.getAttribute("sessionUserId")));
	}
	
	@RequestMapping("delete")
	protected String delete(WebRequest req, Model model) {
		alarmDao.delete(req.getParameter("alarmId"));
		return "redirect:/search/n/"+req.getParameter("noteId");
	}
}
