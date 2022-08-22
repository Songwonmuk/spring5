package net.softsociety.spring5.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.spring5.dao.MemberDAO;
import net.softsociety.spring5.domain.Member;

/**
 * MemberService 인터페이스의 구현체
 */
@Service
@Slf4j
public class MemberSeviceImpl implements MemberService {

    @Autowired
    private MemberDAO memberDAO;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public int insertMember(Member member) {
		String encodedPassword = passwordEncoder.encode(member.getMemberpw());
		log.debug("암호화된 비번 {} : {}", member.getMemberpw(), encodedPassword);
		member.setMemberpw(encodedPassword);
		
		int result = memberDAO.insert(member);
		return result;
	}

	@Override
	public boolean idcheck(String memberid) {
		Member member = memberDAO.selectOne(memberid);
		return member == null? true : false;
	}

	@Override
	public Member getMemberInfo(String username) {
		Member member = memberDAO.selectOne(username);
		return member;
	}

	@Override
	public int updateMember(Member member) {
		if (member.getMemberpw() != null && member.getMemberpw().length() != 0) {
			String encodedPassword = passwordEncoder.encode(member.getMemberpw());
			member.setMemberpw(encodedPassword);
		}

		int result = memberDAO.update(member);
		return result;
	}

	
	

}
