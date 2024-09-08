package project.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.board.config.auth.PrincipalDetails;
import project.board.dto.SignUpRequestDTO;
import project.board.model.Member;
import project.board.model.Role;
import project.board.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MemberRepository memberRepository;

	public void save(SignUpRequestDTO dto) {
		Member member = Member.builder()
				.username(dto.getUsername())
				.email(dto.getEmail())
				.password(bCryptPasswordEncoder.encode(dto.getPassword()))
				.role(Role.ROLE_USER)
				.provider("default")
				.providerId("default")
				.build();
		memberRepository.save(member);
	}

	public UserDetails findUserByEmail(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
		return new PrincipalDetails(member.getEmail(), member.getPassword(), member.getRole());
	}
}
