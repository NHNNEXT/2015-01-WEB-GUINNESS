package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.NOTELIST_READ)
public class ReadNoteListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String groupId = req.getParameter("groupId");
		DateTime targetDate = new DateTime(req.getParameter("targetDate")).plusDays(1).minusSeconds(1);
		DateTime endDate = targetDate.minus(Period.days(10));
		PrintWriter out = resp.getWriter();
		List<Note> noteList = null;
		logger.debug("start endDate={} targetDate={}", endDate, targetDate);
		try {
			noteList = NoteDao.getInstance().readNoteList(groupId, endDate.toString(), targetDate.toString());
		} catch (Exception e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
		}
		out.print(new Gson().toJson(noteList));
		out.close();
	}
}
