package net.softsociety.spring5.service;

import java.util.ArrayList;

import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.domain.Reply;
import net.softsociety.spring5.util.PageNavigator;

public interface BoardService {
	//글 저장
	public int write(Board board);
	//글 읽기
	public Board read(int boardnum);
	//페이징 정보
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String searchWord);
	//현재 페이지 글 목록
	public ArrayList<Board> list(PageNavigator navi, String type, String searchWord);
	//글 삭제
	public int delete(Board board);
	//글 수정
	public int update(Board board);
	//리플 저장
	public int writeReply(Reply reply);
	//리플 목록
	public ArrayList<Reply> listReply(int boardnum);
	//리플 삭제
	public int deleteReply(Reply reply);

}
