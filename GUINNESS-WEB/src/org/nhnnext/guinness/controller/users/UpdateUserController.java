package org.nhnnext.guinness.controller.users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UpdateUserController {
	
	@RequestMapping(value="/user/update")
	protected String getPage(HttpServletRequest req, HttpServletResponse resp) {
		return "updateUser";
	}
}