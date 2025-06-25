package com.tenco.blog.board;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository // IoC + 싱글톤 패턴 관리
@RequiredArgsConstructor // 생성자 자동 생성 + 멤버 변수 -> DI 처리 됨
public class BoardRepository {

    private final EntityManager entityManager;

    /*
    * 전체 게시글 조회
    * */
    public List<Board> findByAll() {
        // 조회 - JPQL 쿼리 선택
        String jpql = "select b from Board b order by b.id desc";
        TypedQuery query = entityManager.createQuery(jpql,Board.class);
        List<Board> boardList = query.getResultList();
        return entityManager.createQuery(jpql,Board.class).getResultList();
    }

    public Board findById(Long id) {
        // 상세 조회
        return entityManager.find(Board.class,id);
    }

    @Transactional
    public Board save(Board board){
        // 비영속 상태의 Board Object 를 영속성 컨텍스트에 저장하면 
        entityManager.persist(board);
        // 이후 시점는 사실 같은 메모리주소를 가리킨다
        return board;
    }

    @Transactional
    public void update(Long id ,BoardRequest.UpdateDTO updateDTO){
        // 객체가 있을 경우
        Board board = findById(id);
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());

    }

    @Transactional
    public void deleteById(Long id){
        Board board = findById(id);
        if(board == null){
            throw new IllegalArgumentException("삭제할 게시글이 없습니다");
        }
        entityManager.remove(board); // 삭제
    }

//    @Transactional
//    public void deleteById(Long id){
//        String jpql = "delete from Board b where b.id = :id";
//        Query query = entityManager.createQuery(jpql);
//        query.setParameter("id",id);
//
//        int Count = query.executeUpdate();
//        if(Count == 0){
//            throw new IllegalArgumentException("삭제할 게시글이 없습니다");
//        }
//    }


}
