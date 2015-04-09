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
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String userId = (String) session.getAttribute("sessionUserId");
		String targetDate = req.getParameter("targetDate") + " " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		if (noteText.equals("")) {
			resp.sendRedirect("/g/" + groupId);
			return;
		}

		try {
			NoteDao.getInstance().createNote(new Note(noteText, targetDate, userId, groupId));
		} catch (SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
