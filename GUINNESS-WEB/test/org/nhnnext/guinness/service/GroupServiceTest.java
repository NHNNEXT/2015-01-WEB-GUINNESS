package org.nhnnext.guinness.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nhnnext.guinness.exception.GroupUpdateException;
import org.nhnnext.guinness.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class GroupServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceTest.class);
	
	@Resource
	GroupService groupService;

	@Test
	public void update() throws GroupUpdateException {
		String sessionUserId = "a@a.a";
		Group group = new Group("cneih", "public", "d@d.d", "T");
//		String rootPath = (new HttpSession()).getServletContext().getRealPath("/");
		groupService.update(sessionUserId, group, null, null);
	}
	
	@Test
	public void 그룹장_탈퇴시도() throws Exception {
		groupService.leaveGroup("d@d.d", "cneih");
	}
	
	@Test
	public void 그룹멤버_아닐때_탈퇴시도() throws Exception {
		groupService.leaveGroup("a@a.a", "cneih");
	}

}
