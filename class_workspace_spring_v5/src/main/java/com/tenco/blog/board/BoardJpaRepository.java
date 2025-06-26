package com.tenco.blog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
* 게시글 관련 데이터베이스 접근을 담당
* 기본적인 CRUD 제공*/
@Repository
public interface BoardJpaRepository extends JpaRepository<Board,Long> {
    // 기보  CRUD 기능 추가적인 기능은 직접 선언해주어야 한다
    
    // 게시글과 사용자 정보가 포함된 엔티를 만들어 주어야 한다
    @Query("select b from Board b join fetch b.user u order by b.id desc")
    List<Board> findAllJoinUser(); 
    // join fetch는 모든 Board 엔티티와 연관된 User를 한방 쿼리로 가져옴
    // LAZY 전략이라서 N + 1 방지를 할 수 있다
    // : 게시글 10개가 있다면 지연 로딩 1(Board 조회) + 10(User 조회) == 11번 쿼리가 발생

    @Query("select b from Board b join fetch b.user where b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id")Long id);
}
