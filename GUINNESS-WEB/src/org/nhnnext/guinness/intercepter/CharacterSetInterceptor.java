package org.nhnnext.guinness.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CharacterSetInterceptor extends HandlerInterceptorAdapter {
	private final String DEFAULT_ENCODING = "UTF-8";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setCharacterEncoding(DEFAULT_ENCODING);
		response.setCharacterEncoding(DEFAULT_ENCODING);
		return true;
	}
}
