package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

@WebServlet(WebServletUrl.NOTE_CREATE)
public class CreateNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate");
		String noteText = req.getParameter("noteText");
		Calendar calendar = Calendar.getInstance();
		targetDate += " " + new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
		
		if (noteText.equals("")) {
			resp.sendRedirect("/g/" + groupId);
			return;
		}

		try {
			new NoteDao().createNote(new Note(noteText, targetDate, userId, groupId));
		} catch (SQLException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, null, null, "/exception.jsp");
			return;
		}
	}
}
