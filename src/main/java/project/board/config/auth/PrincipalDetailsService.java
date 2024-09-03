package project.board.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.board.dto.MemberDTO;
import project.board.model.Member;
import project.board.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
		});
		MemberDTO memberDTO = new MemberDTO(member);
		return new PrincipalDetails(memberDTO);
	}
}
