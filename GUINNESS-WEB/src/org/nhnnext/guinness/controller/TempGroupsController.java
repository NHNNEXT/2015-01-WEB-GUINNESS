package org.nhnnext.guinness.controller;

import org.nhnnext.guinness.model.Group;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/groups")
public class TempGroupsController {
	@RequestMapping(value={"", "/"})
	public String list(){
		return "groups";
	}
	
	@RequestMapping(value={"/form", "/createForm"})
	public String createForm(){
		return "form";
	}
	
	@RequestMapping(value={"/{groupId}/form"})
	public String updateForm(@PathVariable Long groupId){
		return "form";
	}
	
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public String create(Group group){
		return "redirect:/groups";
	}
	
	@RequestMapping("/{groupId}")
	public String show(@PathVariable Long groupId){
		return "show";
	}
}
