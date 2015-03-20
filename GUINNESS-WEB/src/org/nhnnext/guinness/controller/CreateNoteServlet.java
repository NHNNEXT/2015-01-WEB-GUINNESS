package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDAO;

@WebServlet("/notes/create")
public class CreateNoteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		String userId = req.getParameter("userId");		
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
		String targetDate = req.getParameter("targetDate");
		
		groupId = "abcde";
		userId = "test@guinness.org";
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, Integer.parseInt(targetDate));

		targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		
		Note note = new Note(noteText, targetDate, userId, groupId);
		
		NoteDAO noteDAO = new NoteDAO();
		
		try {
			noteDAO.createNote(note);
			resp.sendRedirect("/notes.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
