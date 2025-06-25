package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardPersistRepository {

    // JPA 핵심 인터페이스
    // @Autowired final 를 사용시 하용 불가
    private final EntityManager em;

    @Transactional
    public void deleteById(Long id){
        Board board = findById(id);
        em.remove(board);
    }

    // 게시글 수정하기 (DB 접근 계층)
    @Transactional
    public void update(Long id , BoardRequest.UpdateDTO updateDTO){

        //트랜잭션
        // 수정 --- select (- 값 확인해서 - 데이터 수정) -- update( 실행)

        Board board = findById(id);
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());
        board.setUsername(updateDTO.getUsername());
        // board -> 영속성 컨텍스트 1차 캐쉬에 Key Value 값이 저장되어있다

        // 트렌잭션 끝나면 영속성 컨텍스트에서 변경 감지를 한다
        // 변경 감지(Dirty Checking)
        // 1. 영속성 컨텍스트가 엔티티 최초 상태를 스냅샷으로 보관
        // 2. 필드 값 변경 시 현재 상태와 스냅샷 비교
        // 3. 트랜잭션 커밋 시점에 변경된 필드만 UPDATE 쿼리를 자동 생성
        // 4. Update board_tb set title=? , content=? , username=? where id =?
    }

    // 게시글 한건 조회(상세조회)
    // em.find(); , JPQL , 네이티브 쿼리
    public Board findById(Long id) {
        return em.find(Board.class, id);
    }

    // JPQL 사용해서 게시글 한건 조회 만들기
    public Board findByIdJPQL(Long id) {
        String jpql = "select b from Board b where b.id = :id";
        try {
            return em.createQuery(jpql, Board.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;

        }
        
        // JPQL 단점 :
        // 1. 1차 캐시 우회하여 항상 DB 접근
        // 2. 코드가 복잡하게 나올 수 있음
        // 3. getSingleResult() 로출 <-- 예외 처리 해주어야 함
    }

    // JPQL을 사용한 게시글 목록 조회
    public List<Board> findAll() {
        // JPQL : 엔티티 객체를 대상으로 하는 객체지향 쿼리
        // Board는 엔티티 클레스명 , b는 별칭
        String jpql = "select b from Board b order by b.id desc";

        // em.createNativeQuery

        return em.createQuery(jpql, Board.class).getResultList();
    }

    @Transactional
    public Board save(Board board) {
        // v1 -> 네이티브 쿼리를 활용 했다.
        // 1. 매개변수로 받은 board는 현재 비영속 상태이다.
        // - 아직 영속성 컨텍스트에 관리 되지 않는 상태
        // - 데이터베이스와 아직은 연관 없는 순수 Java 객체 상태

        // 2. em.persist(board); - 이 엔티티를 영속성 컨텍스트에 저장하는 개념이다 
        // - 영속성 컨텍스트가 board 객체를 관리하게 된다
        em.persist(board);

        // 3. 트랜잭션 커밋 시점에 Insert 쿼리 실행
        // - 이때 연속성 컨텍스트의 변경 사항이 자동으로 DB에 반영 됨
        // - board 객체의 id 필드에 자동으로 생성된 값이 설정 됨
        return board;
    }


}
