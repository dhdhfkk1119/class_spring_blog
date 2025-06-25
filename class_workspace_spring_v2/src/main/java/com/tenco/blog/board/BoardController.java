package com.tenco.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardPersistRepository boardPersistRepository;

    @GetMapping("/board/save-form")
    public String saveForm(){
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String saveForm(BoardRequest.SaveDTO reqDTO){
        // HTTP 요청 본문 : title=값&content=값&username=값
        // form 태그의 MIME 타입 ( application/x-www-form-urlencoded )

        // Board board = new Board(reqDTO.getTitle(),reqDTO.getContent(),reqDTO.getUsername());
        Board board = reqDTO.toEntity();
        boardPersistRepository.save(board);

        return "redirect:/";
    }

    @GetMapping({"/","/index"})
    public String index(Model model){
        List<Board> boards = boardPersistRepository.findAll();
        model.addAttribute("boardList",boards);
        return "index";
    }
    
    // 게시글 상세보기
    // 주소설계

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id")Long id,HttpServletRequest request){
        Board board = boardPersistRepository.findById(id);
        request.setAttribute("board",board);
        return "board/detail";
    }

    // 게시글 수정하기 GET 형식
    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long id ,HttpServletRequest request){
        Board board = boardPersistRepository.findById(id);
        request.setAttribute("board",board);
        return "board/board-update";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id , BoardRequest.UpdateDTO request){
        System.out.println(request);
        boardPersistRepository.update(id,request);
        return "redirect:/";
    }
    
    // 주소 설계
    @PostMapping("/board/{id}/delete-form")
    public String delete(@PathVariable(name = "id")Long id){
        boardPersistRepository.deleteById(id);
        return "redirect:/";
    }

}
