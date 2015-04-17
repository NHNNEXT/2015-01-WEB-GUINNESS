package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
public class ReadNoteController{
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteController.class);
	@Autowired
	private NoteDao noteDao;
	
	@RequestMapping("/note/read")
	protected void excute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");
		Note note = null;
		try {
			note = noteDao.readNote(paramsList.get("noteId"));
		} catch (MakingObjectListFromJdbcException e) {
			logger.error("Exception", e);
			Forwarding.forwardForException(req, resp);
		}
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(note));
		out.close();
	}
}
