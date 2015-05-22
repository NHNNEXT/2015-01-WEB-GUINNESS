package org.nhnnext.guinness.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class ServletRequestUtilTest {

	@Test
	public void existedUserIdFromSession() throws IOException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpSession session = mock(HttpSession.class);
		
		when(req.getSession()).thenReturn(session);
		when(session.getAttribute("sessionUserId")).thenReturn("testUserId");
		
		Boolean bool = ServletRequestUtil.existedUserIdFromSession(session);
		assertEquals(true, bool);
	}
	
	@Test
	public void getIP() throws UnknownHostException {
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		System.out.println(InetAddress.getLocalHost().getHostAddress().equals("10.73.43.171"));
	}

}
