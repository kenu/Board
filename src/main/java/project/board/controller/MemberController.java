package project.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.board.dto.MemberRequestDTO;
import project.board.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@GetMapping({"/", "/board"})
	public String index() {
		return "board/index";
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "member/login";
	}

	@GetMapping("/signupForm")
	public String signupForm(Model model) {
		model.addAttribute("dto", new MemberRequestDTO());
		return "member/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute MemberRequestDTO dto) {
		log.info("회원가입 성공");
		log.info("dto.getEmail={}", dto.getEmail());
		log.info("dto.getPassword={}", dto.getPassword());
		log.info("dto.getUsername={}", dto.getUsername());
		memberService.save(dto);
		return "redirect:/board/index";
	}
}
