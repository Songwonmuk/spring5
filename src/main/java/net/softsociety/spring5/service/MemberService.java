package net.softsociety.spring5.service;

import net.softsociety.spring5.domain.Member;

/** 
 * 회원정보 관련 서비스 인터페이스
 */
public interface MemberService {
	/**
	 * 회원가입
	 * @param member 저장할 회원 정보
	 * @return 저장된 행 개수
	 */
	public int insertMember(Member member);
	
	/**
	 * 	아이디 중복 확인
	 * @param memberid
	 * @return
	 */
	public boolean idcheck(String memberid);

	/**
	 * 아이디로 회원정보를 검색
	 * @param username 검색할 아이디
	 * @return 회원정보
	 */
	public Member getMemberInfo(String username);

	/**
	 * 회원정보 수정
	 * @param member 수정할 정보
	 * @return 수정된 행 개수
	 */
	public int updateMember(Member member);
	
	

}
