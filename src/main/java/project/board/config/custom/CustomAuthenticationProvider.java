package project.board.config.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import project.board.config.auth.PrincipalDetails;
import project.board.config.auth.PrincipalDetailsService;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final PrincipalDetailsService principalDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public CustomAuthenticationProvider(PrincipalDetailsService principalDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.principalDetailsService = principalDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		try {
			UserDetails userDetails = principalDetailsService.loadUserByUsername(email);
			PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
			if (bCryptPasswordEncoder.matches(password, principalDetails.getPassword())) {
				log.info("로그인 아이디={}, 패스워드={}", email, password);
				return new UsernamePasswordAuthenticationToken(email, password, principalDetails.getAuthorities());
			}
		} catch (UsernameNotFoundException e) {
			log.error("사용자를 찾을 수 없습니다. 회원가입을 해주세요.");
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. 회원가입을 해주세요.");
		}
		log.error("아이디 혹은 패스워드를 다시 확인해주세요.");
		throw new BadCredentialsException("아이디 혹은 패스워드를 다시 확인해주세요.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
