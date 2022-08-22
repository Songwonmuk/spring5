package net.softsociety.spring5.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 (시큐리티를 사용한 회원 인증에 사용)
 * UserDetails 인터페이스를 implements
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
	private static final long serialVersionUID = 3222388532456457383L;

	String memberid;			//사용자 식별ID
	String memberpw;			//비밀번호
	String membername;			//사용자 이름
	String email;				//이메일주소
	String phone;				//전화번호
	String address;				//집주소
	boolean enabled;			//계정상태. 1:사용가능, 0:사용불가능
	String rolename;			//사용자 구분. 'ROLE_USER', 'ROLE_ADMIN'중 하나
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getPassword() {
		return this.memberpw;
	}
	
	@Override
	public String getUsername() {
		return this.memberid;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
