package org.nhnnext.guinness.controller;

import java.io.IOException;

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

@Controller
public class AlarmController {

	@Resource
	private AlarmDao alarmDao;

	@RequestMapping("/alarms")
	protected @ResponseBody JsonResult list(HttpSession session) {
		String userId = (String) session.getAttribute("sessionUserId");
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.list(userId));
	}

	@RequestMapping("/alarm/delete")
	protected String delete(WebRequest req, Model model) {
		alarmDao.delete(req.getParameter("alarmId"));
		return "redirect:/search/n/" + req.getParameter("noteId");
	}

	@RequestMapping("/alarm/counts")
	protected @ResponseBody JsonResult alarmCounts(HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.readNoteAlarm(sessionUserId));
	}
}