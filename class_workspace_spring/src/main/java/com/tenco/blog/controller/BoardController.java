package com.tenco.blog.controller;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import jakarta.servlet.http.HttpServlet;
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
public class BoardController {

    private BoardNativeRepository boardNativeRepository;

    public BoardController(BoardNativeRepository boardNativeRepository){
        this.boardNativeRepository = boardNativeRepository;
    }

    // board 작성하기
    @PostMapping("/board/save")
    public String save(
            @RequestParam(name = "username") String username ,
            @RequestParam(name = "content") String content ,
            @RequestParam(name = "title") String title) {
        // username, title, content <--- DTO 받는 방법 , 기본 데이터 타입 설정
//        System.out.println(board.getTitle());
//        System.out.println(board.getUsername());
//        System.out.println(board.getContent());
        System.out.println(username);
        System.out.println(content);
        System.out.println(title);
        boardNativeRepository.save(title,content,username);

        return "redirect:/";
    }

    @GetMapping({"/","/index"})
    public String index(HttpServletRequest httpServletRequest){
        // prefix: /templates/
        // return : index
        // suffix : .mustache
        // # 기본경로 : src/main/resource/templates/index.mustache
        List<Board> boardList = boardNativeRepository.findAll();

        httpServletRequest.setAttribute("boardList",boardList);
        return "index";
    }


    /*
    * 게시물 작성하기
    * getMapping로 템플릿 가져오기*/
    
    @GetMapping("/board/save-form")
    public String showSaveForm() {
        // Save form view를 반환
        return "board/save-form";
    }
    
    /*
    * 게시물 상세보기 요청
    * board/1
    * */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id,HttpServletRequest request){
        // URL 에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id값으로 데이터베이스에 있는 게시글 조회하고
        // 머스테치 파일로 데이터를 받아온다
        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board",board);

        return "board/detail";
    }

    @PostMapping("/board/{id}/delete-form")
    public String delete(@PathVariable(name = "id") Long id){
        // URL 에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id값으로 데이터베이스에 있는 게시글 조회하고
        // 머스테치 파일로 데이터를 받아온다
        boardNativeRepository.deleteById(id);;

        return "redirect:/";
    }

    @GetMapping("/board/{id}/board-update")
    public String update(@PathVariable(name = "id") Long id,HttpServletRequest request){
        // URL 에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id값으로 데이터베이스에 있는 게시글 조회하고
        // 머스테치 파일로 데이터를 받아온다
        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);
        return "board/board-update";
    }

    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,
                         @RequestParam(name = "username") String username,
                         @RequestParam(name = "content") String content,
                         @RequestParam(name = "title") String title){
        // URL 에서 받은 id 값을 사용해서 특정 게시글 상세보기 조회
        // 실제로는 이 id값으로 데이터베이스에 있는 게시글 조회하고
        // 머스테치 파일로 데이터를 받아온다

        boardNativeRepository.updateById(id,username,content,title);

        return "redirect:/board/" + id;
    }








}
