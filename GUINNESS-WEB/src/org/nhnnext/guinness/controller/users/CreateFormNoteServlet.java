package org.nhnnext.guinness.controller.users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Note;

@WebServlet("/notes/createForm")
public class CreateFormNoteServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setAttribute("note", new Note());
		RequestDispatcher rd = req.getRequestDispatcher("/newNote.jsp");
		rd.forward(req, resp);
		
	}
}
