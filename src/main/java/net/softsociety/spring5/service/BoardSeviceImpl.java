package net.softsociety.spring5.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.dao.BoardDAO;
import net.softsociety.spring5.domain.Board;
import net.softsociety.spring5.domain.Reply;
import net.softsociety.spring5.util.PageNavigator;

@Transactional
@Service
@Slf4j
public class BoardSeviceImpl implements BoardService {

    @Autowired
    private BoardDAO boardDAO;

	@Override
	public int write(Board board) {
		int result = boardDAO.insertBoard(board);
		return result;
	}

	@Override
	public Board read(int boardnum) {
		int result = boardDAO.updateHits(boardnum);
		Board board = boardDAO.selectBoard(boardnum);
		return board;
	}

	@Override
	public PageNavigator getPageNavigator(
			int pagePerGroup, int countPerPage, int page, String type, String searchWord) {
		
		HashMap<String, String> map = new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		
		int total = boardDAO.countBoard(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}

	@Override
	public ArrayList<Board> list(PageNavigator navi, String type, String searchWord) {
		
		HashMap<String, String> map =new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		ArrayList<Board> boardlist = boardDAO.selectBoardList(map, rb); 
		
		return boardlist;
	}

	@Override
	public int delete(Board board) {
		int result = boardDAO.deleteBoard(board);
		return result;
	}

	@Override
	public int update(Board board) {
		int result = boardDAO.updateBoard(board);
		return result;
	}

	@Override
	public int writeReply(Reply reply) {
		int result = boardDAO.insertReply(reply);
		return result;
	}

	@Override
	public ArrayList<Reply> listReply(int boardnum) {
		ArrayList<Reply> replylist = boardDAO.selectReplyList(boardnum);
		return replylist;
	}

	@Override
	public int deleteReply(Reply reply) {
		int result = boardDAO.deleteReply(reply);
		return result;
	}
	
}
