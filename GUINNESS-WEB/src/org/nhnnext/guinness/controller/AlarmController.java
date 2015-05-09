package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/alarm")
public class AlarmController {
	
	@Resource
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
	
	@RequestMapping("counts")
	protected @ResponseBody JsonResult<Map<String, Object>> alarmCounts (HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult<Map<String, Object>>().setSuccess(true).setListValues(alarmDao.readNoteAlarm(sessionUserId));
	}
}