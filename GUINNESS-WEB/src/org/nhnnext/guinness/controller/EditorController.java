package org.nhnnext.guinness.controller;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Group;
import org.nhnnext.guinness.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

@Controller
public class EditorController {
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private NoteDao noteDao;

	@RequestMapping(value = "/editor/g/{groupId}")
	private String createEditor(@PathVariable String groupId, Model model) {
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupName", new Gson().toJson(groupDao.readGroup(groupId).getGroupName()));
		return "editor";
	}

	@RequestMapping(value = "/editor/{noteId}")
	private String updateEditor(@PathVariable String noteId, Model model) {
		Note note = noteDao.readNote(noteId);
		Group group = groupDao.readGroup(note.getGroup().getGroupId());

		model.addAttribute("noteId", noteId);
		model.addAttribute("noteText", note.getNoteText());

		model.addAttribute("groupId", group.getGroupId());
		model.addAttribute("groupName", new Gson().toJson(group.getGroupName()));

		return "editor";
	}
}
