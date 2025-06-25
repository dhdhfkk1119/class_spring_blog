package com.tenco.blog.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Table(name = "user_tb")
@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름 중복 방지를 위한 유니크 제약 설정
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    
    private String password;
        
    // now() -> X
    // 엔티티가 영속화 할 떄 자동으로 pc 현재 시간을 설정해 준다
    @CreationTimestamp
    private Timestamp createdAt;
    
    // 객치 생성시 가독성과 안정성 향상
    @Builder
    public User(Long id, String username, String email, String password, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
}
