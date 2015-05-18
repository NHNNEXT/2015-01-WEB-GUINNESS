package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.AlarmDao;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AlarmController {

	//TODO 코트리뷰 서비스로 레이어를 분리해야할지?
	// 선택의 문제라 판단함. 일관성 측면에서 분리할 수도 있고, 효율성 측면에서 현재 상태도 문제 없을 수 있음.
	@Resource
	private AlarmDao alarmDao;

	@RequestMapping("/alarms")
	protected @ResponseBody JsonResult list(HttpSession session) {
		String userId = (String) session.getAttribute("sessionUserId");
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.list(userId));
	}

	@RequestMapping(value = "/alarms/{alarmId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String alarmId, WebRequest req, Model model) {
		alarmDao.delete(alarmId);
		return new JsonResult().setSuccess(true);
	}

	@RequestMapping("/alarms/count")
	protected @ResponseBody JsonResult alarmCounts(HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		return new JsonResult().setSuccess(true).setMapValues(alarmDao.readNoteAlarm(sessionUserId));
	}
}