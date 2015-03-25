package org.nhnnext.guinness.controller.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.ParameterKey;
import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.GroupDao;

import com.google.gson.Gson;

@WebServlet(WebServletURL.GROUP_READ)
public class ReadGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -4587604819173641951L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute(ParameterKey.SESSION_USERID);
		
		// 세션이 없을 경우 루트화면으로 이동 
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}

		// DAO를 이용해 그룹유저맵에서 유저가 속한 그룹의 아이디를 받아온다.
		GroupDao groupDao = new GroupDao();
		ArrayList<Group> groupList = null;
		try {
			groupList = groupDao.readGroupList(userId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		// 받아온 그룹아이디 출력 테스트
		createJsonFile(groupList, resp);
	}

	public void createJsonFile(ArrayList<Group> groupList, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();
		
		sb.append(gson.toJson(groupList));
		out.write(sb.toString());
		out.close();
	}
}
