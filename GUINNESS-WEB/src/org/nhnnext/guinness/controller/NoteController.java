package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nhnnext.guinness.exception.note.UnpermittedAccessNotesException;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.Preview;
import org.nhnnext.guinness.service.GroupService;
import org.nhnnext.guinness.service.NoteService;
import org.nhnnext.guinness.service.PCommentService;
import org.nhnnext.guinness.service.PreviewService;
import org.nhnnext.guinness.service.TempNoteService;
import org.nhnnext.guinness.util.DateTimeUtil;
import org.nhnnext.guinness.util.JSONResponseUtil;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.Markdown;
import org.nhnnext.guinness.util.ReconnectPComments;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Resource
	private TempNoteService tempNoteService;
	
	@RequestMapping("/g/{groupId}")
	protected String initReadNoteList(@PathVariable String groupId, HttpSession session, Model model) throws IOException{
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		model.addAttribute("group", groupService.readGroup(groupId));
		model.addAttribute("noteList", new Gson().toJson(previewService.initNotes(sessionUserId, groupId)));
		return "notes";
	}

	@RequestMapping("/notes/reload")
	protected ResponseEntity<Object> reloadNotes(@RequestParam String groupId, @RequestParam String noteTargetDate) {
		logger.debug("noteTargetDate:{}", noteTargetDate);
		if (groupId == null) {
			throw new UnpermittedAccessNotesException();
		}
		if ("undefined".equals(noteTargetDate))
			noteTargetDate = null;
		return JSONResponseUtil.getJSONResponse(previewService.reloadPreviews(groupId, noteTargetDate), HttpStatus.OK);
	}

	@RequestMapping("/notes/{noteId}")
	protected ResponseEntity<Object> show(@PathVariable String noteId, HttpSession session) throws IOException{
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.readNote(sessionUserId, noteId);
		note.setNoteText(new Markdown().toHTML(note.getNoteText()));
		return JSONResponseUtil.getJSONResponse(note, HttpStatus.OK);
	}

	//TODO Note객체로 묶어서 받아오기
	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(@RequestParam String tempNoteId, @RequestParam String noteText, @RequestParam String noteTargetDate, @RequestParam String groupId, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/g/" + groupId;
		}
		noteService.create(sessionUserId, groupId, noteText, DateTimeUtil.addCurrentTime(noteTargetDate), tempNoteId);
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(@RequestParam String groupId, @RequestParam String noteId, @RequestParam String noteTargetDate, @RequestParam String noteText, HttpSession session) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.readNote(sessionUserId, noteId);
		if(!sessionUserId.equals(note.getUser().getUserId())){
			throw new Exception("불일치");
		}
		String editedNoteTextToMarkdown = new Markdown().toHTML(noteText);
		String originNoteTextToMarkdown = new Markdown().toHTML(note.getNoteText());

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
		List<Map<String, Object>> pCommentList = pCommentService.listByNoteId( noteId);
		pCommentList = ReconnectPComments.UpdateParagraphId(originTextParagraph, editedTextParagraph, pCommentList);
		noteService.update(noteText, noteId, DateTimeUtil.addCurrentTime(noteTargetDate), pCommentList);
		return "redirect:/g/" + groupId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable String noteId) {
		logger.debug(" noteId : " + noteId);
		if (noteService.delete(noteId) != 1) {
			return JSONResponseUtil.getJSONResponse("", HttpStatus.BAD_REQUEST);
		}
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping("/notes/editor/g/{groupId}")
	private String createForm(@PathVariable String groupId, Model model, HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		noteService.checkJoinedGroup(groupId, sessionUserId);
		model.addAttribute("group", groupService.readGroup(groupId));
		model.addAttribute("tempNotes", new Gson().toJson(tempNoteService.read(sessionUserId)));
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable String noteId, Model model, HttpSession session) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.readNote(sessionUserId, noteId);
		if(!sessionUserId.equals(note.getUser().getUserId())){
			throw new Exception("노트 작성자, 수정자 불일치 예외.");
		}
		model.addAttribute("note", note);
		model.addAttribute("group", groupService.readGroup(note.getGroup().getGroupId()));
		model.addAttribute("tempNotes", new Gson().toJson(tempNoteService.read(note.getUser().getUserId())));
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
	
	@RequestMapping(value = "/notes/temp", method = RequestMethod.POST)
	private @ResponseBody JsonResult tempNoteCreate(@RequestParam String noteText,
			@RequestParam String createDate, HttpSession session) throws IOException {
		logger.debug("noteText : {}, createDate : {}", noteText, createDate);
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		long tempNoteId = tempNoteService.create(noteText, DateTimeUtil.addCurrentTime(createDate), sessionUserId);
		return new JsonResult().setSuccess(true).setObject(tempNoteId);
	}
	
	@RequestMapping("/notes/temp/{noteId}")
	protected @ResponseBody JsonResult<Preview> tempNoteRead(@PathVariable long noteId) {
		logger.debug("noteId:{}", noteId);
		return new JsonResult().setSuccess(true).setObject(tempNoteService.readByNoteId(noteId));
	}
	
	@RequestMapping(value = "/notes/temp", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult<Preview> tempNoteUpdate(@RequestParam long noteId, @RequestParam String noteText, 
			@RequestParam String createDate) {
		logger.debug("noteId:{}", noteId);
		
		
		if(tempNoteService.update(noteId, noteText, DateTimeUtil.addCurrentTime(createDate))) {
			return new JsonResult().setSuccess(true).setObject(tempNoteService.readByNoteId(noteId));
		}
		return new JsonResult().setSuccess(false);
	}
	
	@RequestMapping(value = "/notes/temp/{noteId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult<Preview> tempNotedelete(@PathVariable long noteId) {
		logger.debug("noteId:{}", noteId);
		if(tempNoteService.delete(noteId)) {
			return new JsonResult().setSuccess(true);
		}
		return new JsonResult().setSuccess(false);
	}
}
