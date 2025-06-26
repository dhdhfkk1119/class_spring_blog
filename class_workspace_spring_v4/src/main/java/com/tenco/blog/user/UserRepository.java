package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    // 생성자 의존 주입 - DI
    // @Autowired // DI
    // final -> 변수에 한 번 값이 할당되면 그 이후 다른 값으로 다시 할당 불가(불변)
    private final EntityManager entityManager;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public User findByUsernameAndPassword(String username , String password){
        // JPQL
        // 필요 하면 직적 예외 처리 설정
        try{
            String jpql = "select u from User u where u.username = : username and u.password = : password";
            TypedQuery typedQuery = entityManager.createQuery(jpql,User.class);
            typedQuery.setParameter("username",username);
            typedQuery.setParameter("password",password);
            return (User) typedQuery.getSingleResult();

        } catch (Exception e) {
            // 일치하는 사용자가 없거나 에러 발생시 null 반환
            // 즉 , 로그인 실패를 의미함
            return null;
        }
    }

    /*
     * 회원 정보 저장 처리
     * @param user (비영속 상태)
     * @return User 엔티티 반환*/
    @Transactional
    public User save(User user) {
        // 매개변수에 들어오는 user object는 비 영속화 된 상태
        log.info("회원 정보 저장하기 시작");
        entityManager.persist(user);
        return user;
    }

    // 사용자명 중복 체크용 조회 가능
    public User findByUsername(String username) {
        // 필요 하면 직적 예외 처리 설정

        // where username = ?
//        String jpql = "select u from User u where u.username = :username";
//        TypedQuery<User> typedQuery = entityManager.createQuery(jpql,User.class);
//        typedQuery.setParameter("username",username);
//        return typedQuery.getSingleResult();
        try {
            String jpql = "select u from User u where u.username = :username";
            return entityManager.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(Long id){
        User user = entityManager.find(User.class,id);
        if(user == null){
            throw new Exception400("사용자가 없습니다");
        }
        return user;
    }

    @Transactional
    public User update(Long id,UserRequest.UpdateDTO updateDTO){
        log.info("회원 정보 수정 시작 - {}",id);
        User user = findById(id);
        user.setEmail(updateDTO.getEmail());
        user.setPassword(updateDTO.getPassword());
        return user;

    }

}
