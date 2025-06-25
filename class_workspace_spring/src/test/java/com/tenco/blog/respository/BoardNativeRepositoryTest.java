package com.tenco.blog.respository;

import com.tenco.blog.model.Board;
import com.tenco.blog.repository.BoardNativeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

// @Import - 테스트에 사용할 수 있도록 해당 클래스 로드 한다
@Import(BoardNativeRepository.class)
@DataJpaTest // JPA 관련 테스트만 로드하는 테스트
public class BoardNativeRepositoryTest {
    
    @Autowired // 자동 의존서 주입
    private BoardNativeRepository boardNativeRepository;

    // DI - 의존서 주입
    //    public  BoardNativeRepositoryTest(BoardNativeRepository boardNativeRepository){
    //        this.boardNativeRepository = boardNativeRepository;
    //    }

//    @Test
//    public void findById_test(){
//        Long id = 1L;
//        Board board = boardNativeRepository.findById(id);
//        Assertions.assertThat(board.getTitle()).isEqualTo("제목12");
//        Assertions.assertThat(board.getUsername()).isNotNull();
//    }
//
//    @Test
//    public void findAll_test(){
//        // given - 테스트를 위한 준비 단계
//        // 게시글 목록 조회 정상 작동 하는지 확인 --> data.sql 파일에 데이터 이미 준비
//
//        // when - 실제 데스트를 하는 행위 (전체 게시글 목록 조회)
//        List<Board> boardList = boardNativeRepository.findAll();
//
//        // then : 결과 검증 (예상하는대로 동작하는지 검증)
//        // Assertions.assertThat(boardList.size()).isEqualTo(1);
//        Assertions.assertThat(boardList.get(0).getUsername()).isEqualTo("ssar");
//    }

    @Test
    public void deleteById_test(){
        Long id = 1L;
        boardNativeRepository.deleteById(id);

        List<Board> boardList = boardNativeRepository.findAll();
        Assertions.assertThat(boardList.size()).isEqualTo(3);

    }
}
