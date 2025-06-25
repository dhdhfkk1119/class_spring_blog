package com.tenco.blog.model;

import com.tenco.blog.util.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data

// @Table : 실제 데이터베이스 테이블 명을 지정할 때 사용
@Table(name = "board_tb") // 데이터베이스에서 테이블 이름을 지정해줌(찾음)
// @Entity : JPA 가 이 클래스를 데이터베이스 테이블과 맵핑하는 객체(엔티티)로 인식
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;
    private String username;
    private Timestamp createdAt;
    
    // 머스테치에서 포현할 시간을 포맷기능을 (행위) 스르로 만들자
    public String getTime(){
        return MyDateUtil.timestampFormat(createdAt);
    }
}
