package com.tenco.blog.board;

import com.tenco.blog._core.common.ApiUtil;
import com.tenco.blog.user.User;
import com.tenco.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardRestController {
    private final BoardService boardService;

    // http://localhost:8080/?page=0&size=10
    @GetMapping("/")
    public ResponseEntity<ApiUtil<List<BoardResponse.MainDTO>>> main(
            @RequestParam(name = "page",defaultValue = "0")int page,
            @RequestParam(name = "size",defaultValue = "5")int size){
            log.info("게시글 목록 조회 API - 페이지 : {} , 크기 : {}",page,size);
            List<BoardResponse.MainDTO> mainDTOList = boardService.list(page,size);
            return ResponseEntity.ok(new ApiUtil<>(mainDTOList));
    }

    // 게시글 상세 보기
    @GetMapping("/api/boards/{id}/detail")
    public ResponseEntity<ApiUtil<BoardResponse.DetailDTO>> detail(@PathVariable(name = "id")Long id,
                                                                   HttpSession httpSession){
        log.info("게시글 목록 조회 API - ID : {}",id);

        User sessionUser = (User) httpSession.getAttribute(Define.SESSION_USER);
        BoardResponse.DetailDTO detailDTO = boardService.detail(id,sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(detailDTO));
    }

    // 게시글 삭제 API
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<ApiUtil<String>> delete(@PathVariable(name = "id")Long id,
                                                  HttpSession httpSession){
        User sessionUser = (User) httpSession.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id,sessionUser);
        return ResponseEntity.ok(new ApiUtil<>("게시글 삭제"));

    }

    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody BoardRequest.SaveDTO saveDTO,HttpSession httpSession){
        log.info("게시글 작성 요청 API - title : {}" , saveDTO.getTitle());
        saveDTO.validate();;
        User sessionUser = (User) httpSession.getAttribute(Define.SESSION_USER);
        BoardResponse.SaveDTO saveBoard = boardService.save(saveDTO,sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiUtil<>(saveBoard));
    }

    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id")Long id,
                                    @RequestBody BoardRequest.UpdateDTO updateDTO,
                                    HttpSession httpSession){
        log.info("게시글 수정 API 호출 - ID : {}" ,id);
        updateDTO.validate();
        User sessionUser = (User) httpSession.getAttribute(Define.SESSION_USER);

        BoardResponse.UpdateDTO updateBoard = boardService.update(id,updateDTO,sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(updateBoard));
    }
}
