package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.ServletName;
import org.nhnnext.guinness.model.GroupDAO;

@WebServlet(ServletName.GROUP_READ)
public class ReadGroupServlet extends HttpServlet {
	private static final long serialVersionUID = -7534646425281084154L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 유저아이디를 받아온다
		//String userId = (String) req.getSession().getAttribute(SessionKey.SESSION_USERID);
		String userId = "test@guinness.org"; // for test
		
		// DAO를 이용해 그룹유저맵에서 유저가 속한 그룹의 아이디를 받아온다.
		GroupDAO groupDao = new GroupDAO();
		List<String> list = new ArrayList<String>();
		list = groupDao.readGroupList(userId);
		
		// 받아온 그룹아이디 출력 테스
		for(String str : list) {
			System.out.println(str);
		}
	}

}
