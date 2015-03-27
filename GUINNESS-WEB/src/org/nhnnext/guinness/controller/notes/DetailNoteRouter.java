package org.nhnnext.guinness.controller.notes;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/note/read/*")
public class DetailNoteRouter extends HttpServlet {
	private static final long serialVersionUID = 1810055739085682471L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/exception.jsp");
		rd.forward(req, resp);
	}
}
