package org.nhnnext.guinness.controller.groups;

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
import org.nhnnext.guinness.model.GroupDao;
import org.nhnnext.guinness.util.Forwarding;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.nhnnext.guinness.util.WebServletUrl;

import com.google.gson.GsonBuilder;

@WebServlet(WebServletUrl.GROUP_READ_MEMBER)
public class ReadGroupMemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupDao groupDao = GroupDao.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId");
		PrintWriter out = resp.getWriter();
		try {
			out.print(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(groupDao.readGroupMember(paramsList.get("groupId"))));
			out.close();
		} catch (MakingObjectListFromJdbcException | SQLException | ClassNotFoundException e) {
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
