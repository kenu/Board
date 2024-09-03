package project.board.dto;

import lombok.Getter;
import project.board.model.Member;
import project.board.model.Role;

import java.io.Serializable;

@Getter
public class MemberDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long id;
	private final String username;
	private final String password;
	private final String email;
	private final Role role;
	private final String provider;
	private final String providerId;

	// Member → MemberDTO
	public MemberDTO(Member member) {
		this.id = member.getId();
		this.password = member.getPassword();
		this.username = member.getUsername();
		this.email = member.getEmail();
		this.role = member.getRole();
		this.provider = member.getProvider();
		this.providerId = member.getProviderId();
	}
}
