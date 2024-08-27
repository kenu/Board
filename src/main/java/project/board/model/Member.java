package project.board.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends TimeEntity {
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

	// 소셜 로그인 시 메일이 중복된 경우 수정 날짜만 업데이트하여 기존 데이터는 보존
	public Member updateModifiedDate() {
		this.onPreUpdate();
		return this;
	}
}
