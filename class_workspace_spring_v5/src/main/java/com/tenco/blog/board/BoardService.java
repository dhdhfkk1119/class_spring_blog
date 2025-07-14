package com.tenco.blog.board;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
// 모든 메서드를 읽기 전용 트랜잭션으로 실행 (findAll , findById 최적화)
// 서능 최적화(변경 감지 비활성화) , 데이터 수정 방지()
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private final BoardJpaRepository boardJpaRepository;

    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO, User sessionUser) {
        // 1. 로그 기록 - 게시글 저장 요청 정보
        log.info("게시글 저장 서비스 처리 시작 - 제목 {}, 작성사 {}", saveDTO.getTitle(), sessionUser.getUsername());
        Board board = saveDTO.toEntity(sessionUser);
        boardJpaRepository.save(board);
        log.info("게시글 저장 완료 - 아이디 {}, 제목 {}", board.getId(), board.getTitle());
        return board;
    }

    public List<Board> findAll() {
        log.info("게시글 조회 서비스 시작");
        List<Board> boardList = boardJpaRepository.findAllJoinUser();
        log.info("게시글 조회 서비스 종료 : {},", boardList.size());
        return boardList;
    }

    public Board findById(Long id) {
        // 1. 로그 기록
        // 2. 데이터 베이스에서 해당 board id 로 조회
        log.info("게시글 상세 조회 서비스 시작");
        Board board = boardJpaRepository.findByIdJoinUser(id)
                .orElseThrow(() -> {
                    log.info("게시글 조회 실패 - ID {}", id);
                    return new Exception404("해당페이지를 찾을수없습니다");
                });
        log.info("게시글 조회 서비스 종료 {}", board.getTitle());
        return board;
    }

    @Transactional
    public Board updateById(Long id, BoardRequest.UpdateDTO updateDTO, User sessionUser) {
        log.info("게시글 업데이트 시작 {}", id);
        Board board = boardJpaRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("게시글 조회 실패 - ID {}", id);
                    return new Exception404("해당페이지를 찾을수없습니다");
                });

        if(!board.isOwner(sessionUser.getId())){
            throw new Exception403("수정 권한이없습니다");
        }

        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());
        boardJpaRepository.save(board);
        log.info("게시글 수정 완료 {}" , board.getId());
        return board;
    }

    @Transactional
    public void delete(Long id,User sessionUser) {
        log.info("게시글 삭제 시작 {}", id);
        Board board = boardJpaRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("게시글 조회 실패 - ID {}", id);
                    return new Exception404("해당페이지를 찾을수없습니다");
                });
        if(!board.isOwner(sessionUser.getId())){
            throw new Exception403("삭제 권한이없습니다");
        }
        boardJpaRepository.delete(board);
        log.info("게시글 삭제 완료 {}", board.getId());
    }

    public Board isCheckBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        if(!board.isOwner(userId)){
            throw new Exception403("수정 권한이 없습니다");
        }
        return board;
    }
}
