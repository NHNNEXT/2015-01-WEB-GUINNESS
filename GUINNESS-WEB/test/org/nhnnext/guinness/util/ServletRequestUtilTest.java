package org.nhnnext.guinness.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class ServletRequestUtilTest {

	@Test
	public void existedUserIdFromSession() throws IOException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		HttpSession session = mock(HttpSession.class);
		
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("sessionUserId")).thenReturn("testUserId");
		
		Boolean bool = ServletRequestUtil.existedUserIdFromSession(req, resp);
		assertEquals(true, bool);
	}

}
