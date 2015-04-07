package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

import com.google.gson.Gson;

@WebServlet(WebServletUrl.GROUP_READ)
public class ReadGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("sessionUserId");

		// 세션이 없을 경우 루트화면으로 이동
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}

		// DAO를 이용해 그룹유저맵에서 유저가 속한 그룹의 아이디를 받아온다.
		GroupDao groupDao = new GroupDao();
		List<Group> groupList = null;
		try {
			groupList = groupDao.readGroupList(userId);
		} catch (SQLException | MakingObjectListFromJdbcException e) {
			e.printStackTrace();
			Forwarding.forwardForError(req, resp, "errorMessage", "접속이 원활하지 않습니다.", "/exception.jsp");
			return;
		}
		// 받아온 그룹아이디 출력 테스트
		createJsonFile(groupList, resp);
	}

	public void createJsonFile(List<Group> groupList, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();

		sb.append(gson.toJson(groupList));
		out.write(sb.toString());
		out.close();
	}
}
