package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;
import org.nhnnext.guinness.common.WebServletUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.NOTELIST_UPDATE)
public class GetResidualNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(GetResidualNoteServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String groupId = req.getParameter("groupId");
		DateTime targetDate = new DateTime(req.getParameter("targetDate")).minusSeconds(1);
		int residualNotes = Integer.parseInt(req.getParameter("residualNotes"));
		PrintWriter out = resp.getWriter();
		out.print(new Gson().toJson(miningResidualNote(groupId, targetDate, residualNotes)));
		out.close();
	}
	
	private List<Note> miningResidualNote(String groupId, DateTime targetDate, int residualNotes) {
		NoteDao noteDAO = new NoteDao();
		List<Note> addNotes = new ArrayList<Note>();
		List<Note> noteList = new ArrayList<Note>();
		DateTime endDate = targetDate.minusDays(10);
		while (true) {
			logger.debug("endDate={} targetDate={} residualNotes={}", endDate, targetDate, residualNotes);
			try {
				addNotes = noteDAO.readNoteList(groupId, endDate.toString(), targetDate.toString());
				if (addNotes.size() > 0) {
					for (Note note : addNotes) {
						noteList.add(note);
					}
				}
				if ((residualNotes>10 && noteList.size() >= 10) || (residualNotes<=10 && noteList.size() == residualNotes)) {
					return noteList;
				}
			} catch (MakingObjectListFromJdbcException | SQLException e) {
				e.printStackTrace();
			}
			targetDate = endDate;
			endDate = targetDate.minus(Period.days(10));
			logger.debug("start={} end={}", endDate, targetDate);
		}
	}
}
