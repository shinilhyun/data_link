package com.enjoybt.data_link.link.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/manage/linkdata/")
public class LinkdataManageController {
	
//	@Autowired
//	private LinkdataManageService linkService;
	
	@RequestMapping(value="linkdataManage.do")
	public String linkdataManage() {
		return "manage/linkdata/linkdataManage";
	}
	
	@RequestMapping(value="analysisManage.do")
	public String analysisManage() {
		return "manage/linkdata/analysisManage";
	}
}
