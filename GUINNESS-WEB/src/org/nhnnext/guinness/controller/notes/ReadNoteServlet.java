package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.NOTE_READ)
public class ReadNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1810055739085682471L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String noteId = req.getParameter("noteId");
		Note note = null;
		try {
			note = NoteDao.getInstance().readNote(noteId);
		} catch (MakingObjectListFromJdbcException | SQLException e) {
			// TODO log로 에러를 남기거나 rethrow 처리한다. http://www.slipp.net/questions/350 문서 참고해 수정
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
		}
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(note));
		out.close();
	}
}
