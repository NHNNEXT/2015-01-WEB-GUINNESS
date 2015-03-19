package org.nhnnext.guinness.controller.users;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDAO;

public class CreateNoteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String userId = req.getParameter("userId");		
		String groupId = req.getParameter("groupId");
		String noteText = req.getParameter("noteText");
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		Date targetDate = formatter.for(req.getParameter("targetDate"));
		String targetDate = req.getParameter("targetDate");
		
		
		Note note = new Note(noteText, targetDate, userId, groupId);
		
		NoteDAO noteDAO = new NoteDAO();
		
		try {
			noteDAO.createNote(note);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
