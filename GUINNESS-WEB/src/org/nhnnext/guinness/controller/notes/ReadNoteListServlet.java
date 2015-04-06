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
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

import com.google.gson.Gson;


@WebServlet("/notelist/read")
public class ReadNoteListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 사용자가 권한이 있는지 검증
		String groupId = req.getParameter("groupId");
		NoteDao noteDAO = new NoteDao();
		List<Note> noteList = null;
		DateTime targetDate = new DateTime(req.getParameter("targetDate"));
		targetDate = targetDate.plus(Period.days(1));
		DateTime endDate = targetDate.minus(Period.days(10));
		
		try {
			noteList = noteDAO.readNoteList(groupId, endDate.toString(), targetDate.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.setContentType("application/json; charset=UTF-8");

		PrintWriter out = resp.getWriter();
		String jsonData;

		Gson gson = new Gson();
		jsonData = gson.toJson(noteList);
		out.print(jsonData);
	}
}
