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

import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.NOTE_READ)
public class DetailNoteRouter extends HttpServlet {
	private static final long serialVersionUID = 1810055739085682471L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String noteId = req.getParameter("noteId");
		NoteDao noteDao = new NoteDao();
		List<Note> note = null;
		try {
	         note = noteDao.readNote(noteId);
        } catch (MakingObjectListFromJdbcException e) {
	        e.printStackTrace();
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();
		sb.append(gson.toJson(note));
		out.write(sb.toString());
		out.close();
	}
}
