package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BoardNativeRepository {

    // JPA의 핵심 인터페이스
    private EntityManager em;

    public BoardNativeRepository(EntityManager em){
        this.em = em;
    }

    @Transactional
    public void save(String title, String content , String username){
        Query query = em.createNativeQuery("insert into board_tb(title,content,username,created_at)" +
                "values (?,?,?,now()) ");
        query.setParameter(1,title);
        query.setParameter(2,content);
        query.setParameter(3,username);

        query.executeUpdate();
    }

    public Board findById(Long id){
        Query query = em.createNativeQuery("select * from board_tb where id = ?",Board.class);

        // SQL Injection 방지 - 파라미터 바인딩
        // 직접 문자열을 연결하지 않고 ? 를 사용해서 안전하게 값 저장을 하고자 한다
        query.setParameter(1,id);

        // query.getSingleResult() -> 단일 결과만 반환하는 메서드
        // 주의 : null 리턴된다면 예외 발생 --> try-catch 처리를 해야 한다
        // 주의 : 혹시 결과가 2개 행의 리턴이 된다면 예외가 발생하게 된다

        return (Board) query.getSingleResult();
    }

    public List<Board> findAll(){
        Query query = em.createNativeQuery("select * from board_tb order by id desc",Board.class);
        
        // query.getResultList() : 여러 행의 결과를 List 객체로 반환
        // query.getSingleResult() ; 단일 결과만 반환 (한 개의 row 데이터만 있을 때)
        List<Board> boardList = query.getResultList();

        return boardList;
        
    }

    @Transactional
    public void deleteById(Long id){
        Query query = em.createNativeQuery("delete from board_tb where id = ?", Board.class);
        query.setParameter(1,id);
        query.executeUpdate();
    }

    @Transactional
    public void updateById(Long id,String title,String content, String username) {
        String sqlStr = "update board_tb set title = ? , content = ? , " +
                "username = ? where id = ?";
        Query query = em.createNativeQuery(sqlStr,Board.class);
        query.setParameter(1,title);
        query.setParameter(2,content);
        query.setParameter(3,username);
        query.setParameter(4,id);
        int updateRows = query.executeUpdate();
        System.out.println("결과 실행 : " + updateRows);
    }
}
