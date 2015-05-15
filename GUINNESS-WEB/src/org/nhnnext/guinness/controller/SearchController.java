package org.nhnnext.guinness.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SearchController {
	@Resource
	private GroupDao groupDao;
	@Resource
	private NoteDao noteDao;
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	private @ResponseBody JsonResult getSearchResult(WebRequest req, HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String [] words = req.getParameter("words").split(" ");
		return new JsonResult().setSuccess(true).setMapValues(noteDao.searchQueryForMap(sessionUserId, words));
	}
	
	@RequestMapping(value="/search/n/{noteId}")
	private String init(@PathVariable String noteId, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		model.addAttribute("functionSelect", "showNote.js");
		model.addAttribute("sessionUserId", sessionUserId);
		model.addAttribute("noteId", noteId);
		return "search";
	}
}
