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

import org.apache.commons.lang3.StringEscapeUtils;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

@WebServlet(WebServletURL.NOTE_CREATE)
public class CreateNoteServlet extends HttpServlet {
	private static final long serialVersionUID = -4786711774618202192L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("sessionUserId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate");
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		targetDate += " " + dateFormat.format(calendar.getTime());
		String noteText = StringEscapeUtils.escapeHtml4(req.getParameter("noteText"));

		
		if(noteText.equals("")) {
			resp.sendRedirect("/g/"+groupId);
			return;
		}
		
		System.out.println("groupId : " + groupId + " targetDate : " + targetDate + " noteText : " + noteText);
		Note note = new Note(noteText, targetDate, userId, groupId);

		NoteDao noteDAO = new NoteDao();
		try {
			noteDAO.createNote(note);
			resp.sendRedirect("/g/"+groupId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
