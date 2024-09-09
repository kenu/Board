package project.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping
    public String boardMain(Model model) {
        // TODO: 게시글 목록을 가져오는 로직 추가
        return "board/main";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        // TODO: 새 게시글 폼에 필요한 데이터 추가
        return "board/new";
    }

    @PostMapping("/new")
    public String createNewPost(/* TODO: 새 게시글 데이터를 받을 파라미터 추가 */) {
        // TODO: 새 게시글 저장 로직 추가
        return "redirect:/board";
    }
}
