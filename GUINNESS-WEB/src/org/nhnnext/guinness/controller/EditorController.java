package org.nhnnext.guinness.controller;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.google.gson.Gson;

@Controller
public class EditorController {
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private NoteDao noteDao;

	@RequestMapping(value = "/note/editor", method = RequestMethod.GET)
	private String Editor(WebRequest req, HttpSession session, Model model) {
		String groupId = req.getParameter("groupId");
		String noteId = req.getParameter("noteId");
		if (noteId != null) {
			model.addAttribute("noteId", noteId);
			model.addAttribute("noteText", noteDao.readNote(noteId).getNoteText());
		}
		if (groupId != null) {
			String groupName = groupDao.readGroup(groupId).getGroupName();
			model.addAttribute("groupId", groupId);
			model.addAttribute("groupName", new Gson().toJson(groupName));
		}
		return "editor";
	}

	@RequestMapping(value = "/editor/{noteId}", method = RequestMethod.GET)
	private String Editor(@PathVariable String noteId, Model model) {
		Note note = noteDao.readNote(noteId);
		Group group = groupDao.readGroup(note.getGroup().getGroupId());

		model.addAttribute("noteId", noteId);
		model.addAttribute("noteText", note.getNoteText());

		model.addAttribute("groupId", group.getGroupId());
		model.addAttribute("groupName", new Gson().toJson(group.getGroupName()));

		return "editor";
	}
}
