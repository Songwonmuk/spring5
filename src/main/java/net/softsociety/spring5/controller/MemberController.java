package net.softsociety.spring5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.domain.Member;
import net.softsociety.spring5.service.MemberService;

/**
 * 회원 정보 관련 콘트롤러
 */

@Slf4j
@RequestMapping("member")
@Controller
public class MemberController {
	
	@Autowired
	MemberService service;
	
	/**
	 * 가입 폼으로 이동
	 */
	@GetMapping("join")
	public String join() {
		return "/memberView/joinForm";
	}
	
	/**
	 * 가입 처리
	 * @param member 사용자가 폼에 입력한 정보
	 */
	@PostMapping("join")
	public String join(Member member) {
		log.debug("가입정보 : {}", member);
		int result = service.insertMember(member);
		
		return "redirect:/";
	}
	
	/**
	 * 아이디 중복 확인 폼으로 이동
	 */
	@GetMapping("idcheck")
	public String idcheck() {
		return "/memberView/idForm";
	}
	
	/**
	 * 아이디 중복 확인
	 * @param searchId 검색할 ID
	 */
	@PostMapping("idcheck")
	public String idcheck(String searchId, Model model) {
		log.debug("검색할 ID : {}", searchId);
		boolean result = service.idcheck(searchId);
		
		model.addAttribute("searchId", searchId);
		model.addAttribute("result", result);
		
		return "/memberView/idForm";
	}
	
    /**
     * 로그인 화면으로 이동
     */
    @GetMapping("/loginForm")
    public String loginForm() {

        return "/memberView/loginForm";
    }

	/**
	 * 개인정보 수정화면으로 이동
	 */
	@GetMapping("mypage")
	public String mypage(@AuthenticationPrincipal UserDetails user, Model model) {
		log.debug("UserDetails 정보 : {}", user);
		Member member = service.getMemberInfo(user.getUsername());
		model.addAttribute("member", member);
		return "/memberView/mypageForm";
	}
	
	/**
	 * 개인정보 수정 처리
	 */
	@PostMapping("mypage")
	public String mypage(@AuthenticationPrincipal UserDetails user, Member member) {
		log.debug("수정할 정보 : {}", member);
		member.setMemberid(user.getUsername());
		int result = service.updateMember(member);
		return "redirect:/";
	}
	
}
