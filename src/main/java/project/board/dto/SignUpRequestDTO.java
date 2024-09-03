package project.board.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO implements Serializable {
	private String username;
	private String email;
	private String password;
}
