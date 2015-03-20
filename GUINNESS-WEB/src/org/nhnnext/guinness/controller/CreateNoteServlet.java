package org.nhnnext.guinness.controller;

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

import net.slipp.LoginServlet;

import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDAO;

@WebServlet("/notes/create")
public class CreateNoteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		//userId는 세션으로 받아온다.
		String userId = req.getParameter("userId");
		userId = "test@guinness.org";
		
		//세션 구현 완료되면 
//		HttpSession session = req.getSession();
//		Object object = session.getAttribute(LoginUserServlet.SESSION_USER_ID);
//		if(object == null){
//			resp.sendRedirect("/");
//			return;
//		}
		
		String groupId = req.getParameter("groupId");
		groupId = "abcde";
		
		String targetDate = req.getParameter("targetDate");
		String noteText = req.getParameter("noteText");
		
		
		
		
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
