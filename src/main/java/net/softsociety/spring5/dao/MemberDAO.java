package net.softsociety.spring5.dao;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.spring5.domain.Member;

/**
 * 회원정보 관련 매퍼 인터페이스
 */
@Mapper
public interface MemberDAO {
    //회원정보 저장
    public int insert(Member member);
    //회원정보 조회
    public Member selectOne(String memberid);
    //회원정보 수정
	public int update(Member member);
    
}
