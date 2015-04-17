package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.dao.NoteDao;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateNoteController {
	@Autowired
	private NoteDao noteDao;

	@RequestMapping(value = "/note/create", method = RequestMethod.POST)
	protected void excute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ServletRequestUtil.existedUserIdFromSession(req, resp)) {
			resp.sendRedirect("/");
			return;
		}
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "noteText",
				"targetDate");
		String targetDate = paramsList.get("targetDate") + " "
				+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

		if (paramsList.get("noteText").equals("")) {
			resp.sendRedirect("/g/" + paramsList.get("groupId"));
			return;
		}

		noteDao.createNote(new Note(paramsList.get("noteText"), targetDate, sessionUserId, paramsList.get("groupId")));
	}
}
