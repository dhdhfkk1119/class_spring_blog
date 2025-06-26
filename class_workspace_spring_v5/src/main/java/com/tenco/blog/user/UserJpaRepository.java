package com.tenco.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {


    // 사용자 명과 비밀번호로 사용자 조회 (로그인용)
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                              @Param("password") String password);

    // 사용자 이름으로 사용자 조회(중복 체크용)
//    @Query(value = "select * from user_tb where username = :username",nativeQuery = true)
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
