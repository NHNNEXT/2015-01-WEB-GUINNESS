package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.NoteDAO;
import org.nhnnext.guinness.model.NoteList;

import com.google.gson.Gson;


@WebServlet("/notelist/read")
public class ReadNoteListServlet extends HttpServlet {
	private static final long serialVersionUID = 2679047171727614860L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//TODO 사용자가 권한이 있는지 검증
		String groupId = req.getParameter("groupId");
		String targetDate = req.getParameter("targetDate");
		
		NoteDAO noteDAO = new NoteDAO();
		NoteList noteList = noteDAO.findByGroupId(groupId,targetDate);
		
		Gson gson = new Gson();
		String jsonData = gson.toJson(noteList.getItems());
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData);		
	}
}
