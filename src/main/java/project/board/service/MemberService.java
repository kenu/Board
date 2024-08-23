package project.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.board.dto.MemberRequestDTO;
import project.board.model.Member;
import project.board.model.Role;
import project.board.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final MemberRepository memberRepository;

	public void save(MemberRequestDTO dto) {
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
}
