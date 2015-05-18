package org.nhnnext.guinness.controller;

import org.nhnnext.guinness.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
	@RequestMapping("/")
	protected String init(Model model) {
		model.addAttribute("user", new User());
		return "index";
	}
}
