package project.board.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	private String provider;
	private String providerId;

	// 회원 정보 수정
	public void update(Member member) {
		this.email = member.getEmail();
		this.password = member.getPassword();
	}

	// 소셜 로그인 정보 수정
	public void update(String email) {
		this.email = email;
	}
}
