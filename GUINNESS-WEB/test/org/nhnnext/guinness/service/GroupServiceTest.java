package org.nhnnext.guinness.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

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
		groupService.update(sessionUserId, group);
	}

}
