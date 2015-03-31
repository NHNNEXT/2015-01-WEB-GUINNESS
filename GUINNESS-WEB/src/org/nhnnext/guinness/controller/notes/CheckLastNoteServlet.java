package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.NoteDao;

@WebServlet("/notelist/check")
public class CheckLastNoteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int noteCount = Integer.parseInt(req.getParameter("noteCount"));
		String groupId = req.getParameter("groupId");
		NoteDao noteDao = new NoteDao();
		PrintWriter out = resp.getWriter();
		try {
			int re = noteDao.checkGroupNotesCount(groupId);
			System.out.println("re : " +re);
			if (noteCount < re){
				System.out.println("k");
				out.print("true");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.print("/exception.jsp");
		}
	}
}
