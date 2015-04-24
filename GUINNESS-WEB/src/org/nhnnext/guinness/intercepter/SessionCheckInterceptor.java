package org.nhnnext.guinness.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SessionCheckInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SessionCheckInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		logger.debug("getRequestURI: {}", request.getRequestURI());
		
		if (!ServletRequestUtil.existedUserIdFromSession(session)) {
			response.sendRedirect("/");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
}
