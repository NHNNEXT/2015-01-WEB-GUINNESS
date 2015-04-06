package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@WebServlet("/notelist/read")
public class ReadNoteListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteListServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoteDao noteDAO = new NoteDao();
		Gson gson = new Gson();
		PrintWriter out = resp.getWriter();
		List<Note> noteList = null;
		String groupId = req.getParameter("groupId");
		DateTime targetDate = new DateTime(req.getParameter("targetDate")).plusHours(23).plusMinutes(59).plusSeconds(59);
		DateTime endDate = targetDate.minus(Period.days(10));
		logger.debug("start groupId={} targetDate={} endDate={}", groupId, targetDate, endDate);

		try {
			noteList = noteDAO.readNoteList(groupId, endDate.toString(), targetDate.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (req.getParameter("residualNotesCount") != null) {
			int residualNotes = Integer.parseInt(req.getParameter("residualNotesCount"))-noteList.size();
			List<Note> addNotes = null;
			while (residualNotes > 0) {
				targetDate = endDate.minus(Period.days(1));
				endDate = targetDate.minus(Period.days(10));
				logger.debug("targetDate={} endDate={} noteSize={} residualNotes={}", targetDate, endDate, noteList.size(), residualNotes);
				try {
					addNotes = noteDAO.readNoteList(groupId, endDate.toString(), targetDate.toString());
					for (Note note : addNotes) {
						noteList.add(note);
					}
					if (noteList.size() >0){
						residualNotes -= noteList.size();
						break;
					}
				} catch (MakingObjectListFromJdbcException | SQLException e) {
					e.printStackTrace();
					out.print("/exception.jsp");
				}
				break;
			}
		}
		resp.setContentType("application/json; charset=UTF-8");
		String jsonData = gson.toJson(noteList);
		out.print(jsonData);
	}
}
