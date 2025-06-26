package com.tenco.blog.board;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository // IoC + 싱글톤 패턴 관리
@RequiredArgsConstructor // 생성자 자동 생성 + 멤버 변수 -> DI 처리 됨
public class BoardRepository {

    private final EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(BoardRepository.class);
    /*
    * 전체 게시글 조회
    * */
    public List<Board> findByAll() {
        log.info("전체 게시글 조회 시작");
        // 조회 - JPQL 쿼리 선택
        String jpql = "select b from Board b order by b.id desc";
        TypedQuery<Board> query = entityManager.createQuery(jpql,Board.class);
        List<Board> boardList = query.getResultList();
        log.info("전체 게시글 조회 시작 갯수 : {} " , boardList);
        return entityManager.createQuery(jpql,Board.class).getResultList();

    }

    public Board findById(Long id) {
        // 상세 조회
        log.info("전체 게시글 조회 시작");
        entityManager.find(Board.class,id);

        return entityManager.find(Board.class,id);

    }

    @Transactional
    public Board save(Board board){
        log.info("게시글 작성 시작");
        // 비영속 상태의 Board Object 를 영속성 컨텍스트에 저장하면 
        entityManager.persist(board);
        // 이후 시점는 사실 같은 메모리주소를 가리킨다
        log.info("게시글 작성 끝");
        return board;
    }

    @Transactional
    public void update(Long id ,BoardRequest.UpdateDTO updateDTO){
        log.info("게시글 업데이트 시작 - ID {}",id);
        // 객체가 있을 경우
        Board board = findById(id);
        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());
        log.info("게시글 업데이트 완료 - ID {}",board.getId());
    }

    @Transactional
    public void deleteById(Long id){
        log.info("게시글 삭제 시작 - ID {}",id);
        Board board = findById(id);
        if(board == null){
            throw new IllegalArgumentException("삭제할 게시글이 없습니다");
        }
        entityManager.remove(board); // 삭제
        log.info("게시글 삭제 완료 - ID {}",id);
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
