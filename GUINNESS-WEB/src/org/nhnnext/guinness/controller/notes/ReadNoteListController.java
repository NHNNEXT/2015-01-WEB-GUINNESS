package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.dao.NoteDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
public class ReadNoteListController{
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListController.class);
	@Autowired
	private NoteDao noteDao;
	
	@RequestMapping("/notelist/read")
	protected void excute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "targetDate");

		DateTime targetDate = new DateTime(paramsList.get("targetDate")).plusDays(1).minusSeconds(1);
		// 임시 : 캘린더가 만들어지기 전까지 임시로 20년 범위로 가져오기.
		// 추후에는 targetDate에 해당하는 하루치 노트들만 불러올 것.
		DateTime endDate = targetDate.minusYears(10);
		targetDate=targetDate.plusYears(10);
		// 임시 : 여기까지.
		PrintWriter out = resp.getWriter();
		List<Note> noteList = null;
		logger.debug("start endDate={} targetDate={}", endDate, targetDate);
		try {
			noteList = noteDao.readNoteList(paramsList.get("groupId"), endDate.toString(), targetDate.toString());
			logger.debug(noteList.toString());
		} catch (Exception e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
		}
		out.print(new Gson().toJson(noteList));
		out.close();
	}
}
