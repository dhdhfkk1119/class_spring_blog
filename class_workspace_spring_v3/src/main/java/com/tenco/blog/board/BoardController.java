package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.PublicKey;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;

    // 전체 게시물 조회
    @GetMapping("/")
    public String index(HttpServletRequest request) {

        // 1. 게시글 목록 조회
        List<Board> boards = boardRepository.findByAll();
        request.setAttribute("boardList", boards);
        return "index";
    }

    // 게시물 상세 조회
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        // 1. 게시글 상세 조회
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/detail";
    }


    @GetMapping("/board/save-form")
    public String saveForm(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form";
        }
        // 로그인된 사용자만 이동
        return "board/save-form";
    }

    // 게시글 저장 액션
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpSession session) {
        try {
            User sessionUser = (User) session.getAttribute("sessionUser");
            if (sessionUser == null) {
                // 로그인 안한 경우 다시 로그인페이지로 리다이렉트처리
                return "redirect:/login-form";
            }
            saveDTO.validate();

            // SaveDTO --> 저장시키기 위헤 --> Board 변환을 해 주어야 함
            boardRepository.save(saveDTO.toEntity(sessionUser));

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("오류 발생 워리워링월9잉");
            return "redirect:/";
        }
    }

//    @PostMapping("/board/save")
//    public String save(Board board, HttpSession session) {
//        try {
//            User sessionUser = (User) session.getAttribute("sessionUser");
//            if (sessionUser == null) {
//                return "redirect:/login-form";
//            }
//
//            // 중요한 부분
//            board.setUser(sessionUser); // 세션에서 작성자 설정
//
//            boardRepository.save(board); // 엔티티 저장
//
//            return "redirect:/";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/";
//        }
//    }

    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long id, HttpServletRequest request,HttpSession httpSession) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        Board board = boardRepository.findById(id);

        if (board == null) {
            throw new IllegalArgumentException("존재 하지 않는 게시물입니다");
        }
        if(sessionUser == null) {
            throw new IllegalArgumentException("로그인 해주시기 바랍니다");
        }
        if(!board.isOwner(sessionUser.getId())){
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }

        request.setAttribute("board", board);
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
                         HttpServletRequest httpServletRequest,
                         HttpSession httpSession) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        Board board = boardRepository.findById(id);
        if (board == null) {
            throw new IllegalArgumentException("존재 하지 않는 게시물입니다");
        }
        if(sessionUser == null) {
            throw new IllegalArgumentException("로그인 해주시기 바랍니다");
        }
        if(!board.isOwner(sessionUser.getId())){
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }
        boardRepository.update(id, updateDTO);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete-form")
    public String delete(@PathVariable(name = "id") Long id, HttpSession httpSession, Model model, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) httpSession.getAttribute("sessionUser");
        if (sessionUser == null) {
            redirectAttributes.addAttribute("loginError", true);
            return "redirect:/login-form";
        }
        Board board = boardRepository.findById(id);
        if (board == null) {
            throw new IllegalArgumentException("이미 삭제된 게시물 입니다");
        }
        if(!board.isOwner(sessionUser.getId())){
            throw new IllegalArgumentException("삭제 권한이 없습니다");
        }
        boardRepository.deleteById(id);
        return "redirect:/";
    }




}
