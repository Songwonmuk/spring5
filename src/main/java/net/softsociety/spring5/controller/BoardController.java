package net.softsociety.spring5.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.domain.Reply;
import net.softsociety.spring5.service.BoardService;
import net.softsociety.spring5.util.FileService;
import net.softsociety.spring5.util.PageNavigator;

/**
 * 게시판 관련 콘트롤러
 */
@Slf4j
@RequestMapping("board")
@Controller
public class BoardController {
	
	//게시판 목록의 페이지당 글 수
	@Value("${user.board.page}")
	int countPerPage;
	
	//게시판 목록의 페이지 이동 링크 수
	@Value("${user.board.group}")
	int pagePerGroup;
	
	//게시판 첨부파일 업로드 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	@Autowired
	BoardService service;
	
	/**
	 * 게시판 글목록으로 이동
	 * @param page 현재 페이지
	 * @param type 검색 대상
	 * @param searchWord 검색어
	 */
	@GetMapping("list")
	public String list(Model model
		, @RequestParam(name = "page", defaultValue = "1") int page
		, String type
		, String searchWord) {
		
		PageNavigator navi = service.getPageNavigator(pagePerGroup, countPerPage, page, type, searchWord);
		
		ArrayList<Board> boardlist = service.list(navi, type, searchWord);
		
		model.addAttribute("navi", navi);
		model.addAttribute("boardlist", boardlist);
		model.addAttribute("type", type);
		model.addAttribute("searchWord", searchWord);
		
		return "/boardView/list";
	}
	
	/**
	 * 글쓰기 폼으로 이동
	 */
	@GetMapping("write")
	public String write() {
		return "/boardView/writeForm";
	}
	
	/**
	 * 글 저장 
	 * @param board 사용자가 폼에 입력한 게시글 정보
	 * @param user 인증정보
	 * @param upload 첨부 파일
	 */
	@PostMapping("write")
	public String write(
			Board board
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile upload) {
		
		log.debug("저장할 글정보 : {}", board);
		log.debug("파일 업로드 경로: {}", uploadPath);
		log.debug("파일 정보: {}", upload);
		
		board.setMemberid(user.getUsername());
		
		//첨부파일이 있는 경우 지정된 경로에 저장하고, 원본 파일명과 저장된 파일명을 Board객체에 세팅
		if (!upload.isEmpty()) {
			String savedfile = FileService.saveFile(upload, uploadPath);
			board.setOriginalfile(upload.getOriginalFilename());
			board.setSavedfile(savedfile);
		}
		
		int result = service.write(board);
		return "redirect:/board/list";
	}
	
	/**
	 * 글 읽기 
	 * @param boardnum 읽을 글번호
	 */
	@GetMapping("read")
	public String read(
			Model model
			, @RequestParam(name="boardnum", defaultValue = "0") int boardnum) { 

		Board board = service.read(boardnum);
		if (board == null) {
			return "redirect:/board/list"; //글이 없으면 목록으로
		}
		
		//현재 글에 달린 리플들
		ArrayList<Reply> replylist = service.listReply(boardnum);
		
		//결과를 모델에 담아서 HTML에서 출력
		model.addAttribute("board", board);
		model.addAttribute("replylist", replylist);
		return "/boardView/read";
	}	
	
	/**
	 * 첨부파일 다운로드 
	 * @param boardnum 본문 글번호
	 */
	@GetMapping("download")
	public String fileDownload(int boardnum, Model model, HttpServletResponse response) {
		//전달된 글 번호로 글 정보 조회
		Board board = service.read(boardnum);
		
		//원래의 파일명
		String originalfile = new String(board.getOriginalfile());
		try {
			response.setHeader("Content-Disposition", " attachment;filename="+ URLEncoder.encode(originalfile, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//저장된 파일 경로
		String fullPath = uploadPath + "/" + board.getSavedfile();
		
		//서버의 파일을 읽을 입력 스트림과 클라이언트에게 전달할 출력스트림
		FileInputStream filein = null;
		ServletOutputStream fileout = null;
		
		try {
			filein = new FileInputStream(fullPath);
			fileout = response.getOutputStream();
			
			//Spring의 파일 관련 유틸 이용하여 출력
			FileCopyUtils.copy(filein, fileout);
			
			filein.close();
			fileout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}	
	
	/**
	 * 글 삭제
	 * @param boardnum 삭제할 글 번호
	 * @param user 인증정보
	 */
	@GetMapping ("delete")
	public String delete(int boardnum, @AuthenticationPrincipal UserDetails user) {
		
		//해당 번호의 글 정보 조회
		Board board = service.read(boardnum);
		
		if (board == null) {
			return "redirect:list";
		}
		
		//첨부된 파일명 확인
		String savedfile = board.getSavedfile();
		
		//로그인 아이디를 board객체에 저장
		board.setMemberid(user.getUsername());
		
		//글 삭제
		int result = service.delete(board);
		
		//글 삭제 성공 and 첨부된 파일이 있는 경우 파일도 삭제
		if (result == 1 && savedfile != null) {
			FileService.deleteFile(uploadPath + "/" + savedfile);
		}
		
		return "redirect:list";
	}
	
	
	/**
	 * 글수정 폼으로 이동
	 * @param boardnum 수정할 글번호
	 * @param user
	 * @param model
	 * @return
	 */
	@GetMapping("update")
	public String update(int boardnum, Model model) {
		
		Board board = service.read(boardnum);
		model.addAttribute("board", board);
		
		return "/boardView/updateForm";
	}
	
	/**
	 * 글 수정 
	 * @param board 수정할 글내용
	 * @param user 인증정보
	 * @param upload 첨부파일 정보
	 */
	@PostMapping("update")
	public String update(
			Board board
			, @AuthenticationPrincipal UserDetails user
			, MultipartFile upload) {
		
		log.debug("저장할 글정보 : {}", board);
		log.debug("파일 정보: {}", upload);
		
		//작성자 아이디 추가
		board.setMemberid(user.getUsername());
		
		Board oldBoard = null;
		String oldSavedfile = null;
		String savedfile = null;
		
		//첨부파일이 있는 경우 기존파일 삭제 후 새 파일 저장
		if (upload != null && !upload.isEmpty()) {
			oldBoard = service.read(board.getBoardnum());
			oldSavedfile = oldBoard == null ? null : oldBoard.getSavedfile();
			
			savedfile = FileService.saveFile(upload, uploadPath);
			board.setOriginalfile(upload.getOriginalFilename());
			board.setSavedfile(savedfile);
			log.debug("새파일:{}, 구파일:{}", savedfile, oldSavedfile);
		}
		
		int result = service.update(board);
		
		//글 수정 성공 and 첨부된 파일이 있는 경우 파일도 삭제
		if (result == 1 && savedfile != null) {
			FileService.deleteFile(uploadPath + "/" + oldSavedfile);
		}
		
		return "redirect:/board/read?boardnum=" + board.getBoardnum();
	}
	
	/**
	 * 리플 저장
	 * @param reply 저장할 리플 정보
	 */
	@PostMapping("writeReply")
	public String writeReply(
		Reply reply
		, @AuthenticationPrincipal UserDetails user) {
		
		reply.setMemberid(user.getUsername());
		
		log.debug("저장할 리플 정보 : {}", reply);
		int result = service.writeReply(reply);
		
		return "redirect:/board/read?boardnum=" + reply.getBoardnum();
	}
	
	/**
	 * 리플 삭제
	 * @param reply 저장할 리플 정보
	 */
	@GetMapping("deleteReply")
	public String replyWrite(
		Reply reply
		, @AuthenticationPrincipal UserDetails user) {
		
		reply.setMemberid(user.getUsername());
		int result = service.deleteReply(reply);
		
		return "redirect:/board/read?boardnum=" + reply.getBoardnum();
	}
	
}
