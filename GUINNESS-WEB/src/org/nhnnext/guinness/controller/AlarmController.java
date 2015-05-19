package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.model.SessionUser;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/alarms")
public class AlarmController {

	//TODO 코트리뷰 서비스로 레이어를 분리해야할지?
	@Resource
	private AlarmDao alarmDao;

	@RequestMapping("")
	protected @ResponseBody JsonResult list(HttpSession session) {
		String userId = ((SessionUser)session.getAttribute("sessionUser")).getUserId();
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.list(userId));
	}

	@RequestMapping(value = "/{alarmId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String alarmId, Model model) {
		alarmDao.delete(alarmId);
		return new JsonResult().setSuccess(true);
	}

	@RequestMapping("/count")
	protected @ResponseBody JsonResult alarmCounts(HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.readNoteAlarm(sessionUserId));
	}
}