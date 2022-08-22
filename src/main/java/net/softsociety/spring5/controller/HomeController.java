package net.softsociety.spring5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 메인화면
 */
@Slf4j
@Controller
public class HomeController {

	/**
	 * 메인화면
	 */
	@GetMapping({"", "/"})
	public String home() {
		return "home";
	}
	
	/**
	 * Security 관련 Thymeleaf 문법 테스트
	 */
	@GetMapping("/thymeleaf")
	public String thymeleaf(Model model) {
		model.addAttribute("id", "aaa");
		model.addAttribute("num", 10);
		
		return "thymeleaf";
	}
	
}
