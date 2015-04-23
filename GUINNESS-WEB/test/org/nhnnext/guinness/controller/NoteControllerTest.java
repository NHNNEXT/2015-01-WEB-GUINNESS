package org.nhnnext.guinness.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class NoteControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(NoteControllerTest.class);
	
	private NoteController notecontroller;
	private HttpSession session;
	private GroupDao groupDao;
	private NoteDao noteDao;

	@Before
	public void setUp() {
		session = mock(HttpSession.class);
		groupDao = mock(GroupDao.class);
		noteDao = mock(NoteDao.class);
		
		notecontroller = new NoteController();
		
		this.notecontroller.setNoteDao(noteDao);
		this.notecontroller.setGroupDao(groupDao);
	}

	@Test
	public void notesRouter() throws Exception {
		String url = "ygNDa";
		Model model = new BindingAwareModelMap();

		when(session.getAttribute("sessionUserId")).thenReturn("das@das.com");
		when(groupDao.checkJoinedGroup("das@das.com", url)).thenReturn(true);
		ModelAndView mav = notecontroller.initReadNoteList(url, session, model);
		
		logger.debug(model.toString());
		logger.debug(mav.toString());
		assertNotNull(mav);
	}
	
}