package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;

import com.google.gson.Gson;

@WebServlet("/note/read")
public class ReadNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ReadNoteServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "noteId");
		Note note = null;
		try {
			note = NoteDao.getInstance().readNote(paramsList.get("noteId"));
		} catch (MakingObjectListFromJdbcException | SQLException | ClassNotFoundException e) {
			logger.error(e.getClass().getSimpleName() + "에서 exception 발생", e);
			Forwarding.forwardForException(req, resp);
		}
		PrintWriter out = resp.getWriter();
		out.write(new Gson().toJson(note));
		out.close();
	}
}
