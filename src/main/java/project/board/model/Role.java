package project.board.model;

import lombok.Getter;

@Getter
public enum Role {
	ROLE_ADMIN("관리자"),
	ROLE_USER("유저");

	private final String role;

	Role(String role) {
		this.role = role;
	}
}
