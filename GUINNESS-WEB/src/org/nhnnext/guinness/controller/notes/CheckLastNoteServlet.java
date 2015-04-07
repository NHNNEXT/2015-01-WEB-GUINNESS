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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.nhnnext.guinness.common.WebServletUrl;

@WebServlet(WebServletUrl.NOTELIST_CHECK)
public class CheckLastNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CheckLastNoteServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String noteCount = req.getParameter("noteCount");
		String groupId = req.getParameter("groupId");
		if (noteCount == null || groupId == null) {
			logger.error("CheckLastNote Parameter NULL!!");
			out.print("/exception.jsp");
		}
		NoteDao noteDao = new NoteDao();
		try {
			int residualNotes =0;
			residualNotes = noteDao.checkGroupNotesCount(groupId)-Integer.parseInt(noteCount);
			out.print(residualNotes);
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
}
