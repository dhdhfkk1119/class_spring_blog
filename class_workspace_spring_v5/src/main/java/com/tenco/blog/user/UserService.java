package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    // 회원가입
    @Transactional // 메서드 레벨에서 쓰기 전용 트랜잭션 활성화
    public User join(UserRequest.JoinDTO joinDTO){
        // 값이 있을경우 오류 발생
        userJpaRepository.findByUsername(joinDTO.getUsername())
                            .ifPresent(user1 -> {throw new Exception400("이미 존재하는 사용자입니다");
                        });
        // User user = userJpaRepository.findByUsername(joinDTO.getUsername())
        //                .orElseThrow(() -> new Exception400("이미 존재하는 사용자입니다")); 
        //                해당 유저가 없을때 예외 발생 따로 빼서 중복체크를 해줘야함

        return userJpaRepository.save(joinDTO.toEntity());
    }

    // 로그인 처리
    public User login(UserRequest.LoginDTO loginDTO){
         return userJpaRepository.findByUsernameAndPassword(loginDTO.getUsername(),loginDTO.getPassword())
                .orElseThrow(() -> new Exception400("존재하지 않습니다"));

    }
    
    /*
    * 사용자 정보 조회
    * */
    public User findById(Long id){
        return userJpaRepository.findById(id).orElseThrow(() -> {
           log.warn("사용자를 찾을 수 없습니다");
           return new Exception404("존재하지 않는 사용자입니다");
        });
    }

    /*회원정보 수정 처리(더티 체킹)*/

    @Transactional
    public User updateById(Long userId, UserRequest.UpdateDTO updateDTO){
        User user = findById(userId);
        user.setEmail(updateDTO.getEmail());
        user.setPassword(updateDTO.getPassword());
        return user;

    }
    
    
}
