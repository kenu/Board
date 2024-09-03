package project.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.board.dto.SignUpRequestDTO;
import project.board.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	// 로그인
	@GetMapping("/auth/login")
	public String authLogin(@RequestParam(value = "error", required = false) String error,
							@RequestParam(value = "exception", required = false) String exception,
							Model model) {
		model.addAttribute("error", exception);
		return "member/login";
	}

	// 회원가입
	@GetMapping("/auth/signup")
	public String authSignup(Model model) {
		model.addAttribute("dto", new SignUpRequestDTO());
		return "member/signup";
	}

	// 회원가입
	@PostMapping("/signup")
	public String signup(@ModelAttribute SignUpRequestDTO dto) {
		log.info("회원가입 성공");
		log.info("dto.getEmail={}", dto.getEmail());
		log.info("dto.getPassword={}", dto.getPassword());
		log.info("dto.getUsername={}", dto.getUsername());
		memberService.save(dto);
		return "redirect:/board/index";
	}

	// 로그아웃
	@PostMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/auth/login";
	}
}
