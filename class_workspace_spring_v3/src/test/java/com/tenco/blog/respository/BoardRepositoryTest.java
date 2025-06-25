package com.tenco.blog.respository;

import com.tenco.blog.board.BoardRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(BoardRepository.class)
public class BoardRepositoryTest {
    public void deleteById(Long id){
        Long deleteId = 1L;

    }
}
