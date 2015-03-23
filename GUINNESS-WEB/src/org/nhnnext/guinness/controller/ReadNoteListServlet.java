package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.model.Note;

import com.google.gson.Gson;


//클라이언트로부터 GroupId를 받아와 해당 그룹의 일지목록을 가져와준다.
@WebServlet("/notelist/read")
public class ReadNoteListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String groupId = req.getParameter("groupId");
		//TODO 사용자가 권한이 있는지 검증
		
		Gson gson = new Gson();
		List<Note> noteList = new ArrayList<Note>();
		
		//TODO DAO와 협력하여 노트리스트를 가져온다.
		Note testNote = new Note("이거슨내용","2015-03-02","유저아이디",groupId);
		Note testNote2 = new Note("이거슨내용","2015-03-02","유저아이디",groupId);
		
		noteList.add(testNote);
		noteList.add(testNote2);
		//
		
		String jsonData = gson.toJson(noteList);
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData);		
	}
}
