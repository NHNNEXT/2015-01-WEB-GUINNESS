package org.nhnnext.guinness.controller.groups;

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
import org.nhnnext.guinness.model.GroupDao;

import com.google.gson.GsonBuilder;

@WebServlet(WebServletUrl.GROUP_READ_MEMBER)
public class ReadGroupMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		GroupDao groupDao = new GroupDao();
		String groupId = req.getParameter("groupId");
		PrintWriter out = resp.getWriter();
		try {
			out.print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(groupDao.readGroupMember(groupId)));
			out.close();
		} catch (MakingObjectListFromJdbcException | SQLException | ClassNotFoundException e) {
			Forwarding.doforwardException(req, resp);
			return;
		}
	}
}
