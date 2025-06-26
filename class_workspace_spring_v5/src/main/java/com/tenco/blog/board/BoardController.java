package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    // 전체 게시물 조회
    @GetMapping("/")
    public String index(Model model) {

        log.info("메인 페이지 요청");
        
        // 1. 게시글 목록 조회
        List<Board> boards = boardService.findAll();
        model.addAttribute("boardList", boards);
        return "index";
    }

    // 게시물 상세 조회
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {

        // 1. 게시글 상세 조회
        Board board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/detail";
    }


    @GetMapping("/board/save-form")
    public String saveForm() {

        // 로그인된 사용자만 이동
        return "board/save-form";
    }

    // 게시글 저장 액션
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpSession session) {

        log.info("로그인 작성 기능 요청 - 제목 {} :" , saveDTO.getTitle());
        
        User sessionUser = (User) session.getAttribute("sessionUser");

        saveDTO.validate();

        // SaveDTO --> 저장시키기 위헤 --> Board 변환을 해 주어야 함
        boardService.save(saveDTO,sessionUser);

        return "redirect:/";
    }


    // 게시물 업데이트 화면
    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long id, Model model,HttpSession session) {
        Board board = boardService.findById(id);
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardService.isCheckBoardOwner(id,sessionUser.getId());
        model.addAttribute("board", board);
        return "board/board-update";
    }

    // 1. 로그인 유무를 판단
    // 2. 로그인 을 하지 않으면 -> 로그인 페이지로
    // 3. 로그인을 했을 경우 -> 게시물에 대한 권한을 체크
    // 4. 권한을 했을경우 해당 게시물의 유저랑 로그인한 유저가 같을경우 -> 수정 완료
    // 5. 다를경우 -> 오류메세지 와 함께 경고창
    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         BoardRequest.UpdateDTO updateDTO,
                         HttpSession httpSession) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        boardService.updateById(id, updateDTO,sessionUser);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete-form")
    public String delete(@PathVariable(name = "id") Long id, HttpSession httpSession) {

        User sessionUser = (User) httpSession.getAttribute("sessionUser");

        boardService.delete(id,sessionUser);
        return "redirect:/";
    }

}
