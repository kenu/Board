package project.board.config.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.board.dto.MemberDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

	private final MemberDTO member;
	private Map<String, Object> attributes;

	// Spring Security 로그인시 사용
	public PrincipalDetails(MemberDTO member) {
		this.member = member;
	}

	// OAuth2.0 로그인시 사용
	public PrincipalDetails(MemberDTO member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new SimpleGrantedAuthority(member.getRole().getRole()));
		return collect;
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
		return true;
	}

	// OAuth 2.0
	// principalName cannot be empty 오류
	@Override
	public String getName() {
		return member.getEmail();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
