package project.board.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import project.board.service.MemberService;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final MemberService memberService;
	private final BCryptPasswordEncoder passwordEncoder;

	public CustomAuthenticationProvider(BCryptPasswordEncoder passwordEncoder, MemberService memberService) {
		this.passwordEncoder = passwordEncoder;
		this.memberService = memberService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		try {
			UserDetails member = memberService.findUserByEmail(username);

			if (!passwordEncoder.matches(password, member.getPassword())) {
				log.error("사용자 정보를 다시 체크하세요.");
				throw new BadCredentialsException("");
			}

			PrincipalDetails principalDetails = (PrincipalDetails) member;
			return new UsernamePasswordAuthenticationToken(principalDetails, password, principalDetails.getAuthorities());
		} catch (UsernameNotFoundException e) {
			log.error("해당 이메일명의 사용자 정보를 찾을 수 없습니다.");
			throw e;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
