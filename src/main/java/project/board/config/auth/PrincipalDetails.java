package project.board.config.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.board.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

	private String email;
	private String password;
	private Role role;
	private Map<String, Object> attributes;

	// Spring Security 로그인시 사용
	public PrincipalDetails(String email, String password, Role role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// OAuth2.0 로그인시 사용
	public PrincipalDetails(String email, Role role, Map<String, Object> attributes) {
		this.email = email;
		this.role = role;
		this.attributes = attributes;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new SimpleGrantedAuthority(this.getRole().getRole()));
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
		return this.email;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
