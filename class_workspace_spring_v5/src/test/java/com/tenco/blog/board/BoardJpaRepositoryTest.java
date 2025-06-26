package com.tenco.blog.board;

import com.tenco.blog.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class BoardJpaRepositoryTest {
    @Autowired
    private BoardJpaRepository boardJpaRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void save_test(){
        User testUser = User.builder()
                .password("1234")
                .email("a@naver.com")
                .username("testUser")
                .build();

        em.persistAndFlush(testUser);

        Board board = Board.builder()
                .title("테스트 제목")
                .content("테스트내용")
                .user(testUser)
                .build();
        Board board1 = boardJpaRepository.save(board);
        System.out.println(board1.getId());


    }
}
