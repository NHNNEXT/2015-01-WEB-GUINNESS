package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nhnnext.guinness.exception.UnpermittedAccessGroupException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.Preview;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.service.NoteService;
import org.nhnnext.guinness.service.PCommentService;
import org.nhnnext.guinness.service.PreviewService;
import org.nhnnext.guinness.util.DateTimeUtil;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.Markdown;
import org.nhnnext.guinness.util.ReconnectPComments;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class NoteController {
	private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Resource
	private NoteService noteService;
	@Resource
	private GroupService groupService;
	@Resource
	private PreviewService previewService;
	@Resource
	private PCommentService pCommentService;
	
	
	@RequestMapping("/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model)
			throws IOException, UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		model.addAttribute("group", groupService.readGroup(groupId));
		model.addAttribute("noteList", new Gson().toJson(previewService.initNotes(sessionUserId, groupId)));
		return "notes";
	}

	@RequestMapping("/notes/reload")
	protected @ResponseBody JsonResult<Preview> reloadNotes(@RequestParam String groupId,
			@RequestParam String noteTargetDate) {
		logger.debug("noteTargetDate:{}", noteTargetDate);
		if (groupId == null) {
			return new JsonResult().setSuccess(false).setMapValues(new ArrayList<Preview>());
		}
		if ("undefined".equals(noteTargetDate))
			noteTargetDate = null;
		return new JsonResult().setSuccess(true)
				.setObjectValues(previewService.reloadPreviews(groupId, noteTargetDate));
	}

	// 여기.
	@RequestMapping("/notes/{noteId}")
	protected @ResponseBody JsonResult show(@PathVariable String noteId) throws IOException {
		Note note = noteService.readNote(noteId);
		note.setNoteText(new Markdown().toHTML(note.getNoteText()));
		return new JsonResult().setSuccess(true).setObject(note);
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(@RequestParam String groupId, @RequestParam String noteText,
			@RequestParam String noteTargetDate, HttpSession session, Model model) throws IOException,
			UnpermittedAccessGroupException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/g/" + groupId;
		}
		noteService.create(sessionUserId, groupId, noteText, DateTimeUtil.addCurrentTime(noteTargetDate));
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(@RequestParam String groupId, @RequestParam String noteId,
			@RequestParam String noteTargetDate, @RequestParam String noteText) throws IOException {
		String editedNoteTextToMarkdown = new Markdown().toHTML(noteText);
		String originNoteTextToMarkdown = new Markdown().toHTML(noteService.readNote(noteId).getNoteText());

		Document editedDoc = Jsoup.parse(editedNoteTextToMarkdown);
		Document originDoc = Jsoup.parse(originNoteTextToMarkdown);

		Elements editedpTags = editedDoc.getElementsByClass("pCommentText");
		Elements originpTags = originDoc.getElementsByClass("pCommentText");

		String[] editedTextParagraph = new String[editedpTags.size()];
		String[] originTextParagraph = new String[originpTags.size()];

		int i = 0, k = 0;
		for (Element pTag : editedpTags) {
			editedTextParagraph[i++] = pTag.text();
		}
		for (Element pTag : originpTags) {
			originTextParagraph[k++] = pTag.text();
		}
		List<Map<String, Object>> pCommentList = pCommentService.list(noteId);
		pCommentList = ReconnectPComments.UpdateParagraphId(originTextParagraph, editedTextParagraph, pCommentList);
		pCommentService.updateParagraphId(pCommentList);
		noteService.update(noteText, noteId, DateTimeUtil.addCurrentTime(noteTargetDate));
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable String noteId) {
		logger.debug(" noteId : " + noteId);
		if (noteService.delete(noteId) == 1) {
			return new JsonResult().setSuccess(true).setObject(noteId);
		}
		return new JsonResult().setSuccess(false);
	}

	@RequestMapping("/notes/editor/g/{groupId}")
	private String createForm(@PathVariable String groupId, Model model, HttpSession session)
			throws UnpermittedAccessGroupException, IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		noteService.checkJoinedGroup(groupId, sessionUserId);
		model.addAttribute("group", groupService.readGroup(groupId));
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable String noteId, Model model) {
		Note note = noteService.readNote(noteId);
		model.addAttribute("note", note);
		model.addAttribute("group", groupService.readGroup(note.getGroup().getGroupId()));
		return "editor";
	}

	@RequestMapping(value = "/notes/editor/preview", method = RequestMethod.POST)
	private @ResponseBody JsonResult preview(@RequestParam String markdown) throws IOException {
		String html = new Markdown().toHTML(markdown);
		return new JsonResult().setSuccess(true).setMessage(html);
	}
	
	@RequestMapping(value = "/notes/getNullDay/{groupId}/{lastDate}")
	private @ResponseBody JsonResult readNullDay(@PathVariable String groupId, @PathVariable String lastDate) throws IOException, ParseException {
		return new JsonResult().setSuccess(true).setObjectValues(noteService.readNullDay(groupId, lastDate));
	}
}
