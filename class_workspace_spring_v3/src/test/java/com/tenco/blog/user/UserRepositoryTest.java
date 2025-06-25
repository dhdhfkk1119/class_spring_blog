package com.tenco.blog.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(UserRepository.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameAndPassword_test(){
        String username = "ssar";
        String password = "1234";
        User user = userRepository.findByUsernameAndPassword(username,password);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo("ssar");
    }

    @Test
    public void save_test(){
        User user = User.builder()
                .username("조정우")
                .email("whwjddn@anver.com")
                .password("123456")
                .build();
        User saveuser = userRepository.save(user);
        // id 할당 여부 확인
        Assertions.assertThat(saveuser.getId()).isNotNull();
        // 데이터가 정상 등록 확인
        Assertions.assertThat(saveuser.getUsername()).isEqualTo("조정우");
        // 원본 User Object 와 영속화된 Object 가 동일한 객체인지(참조) 확인
        // 영속성 컨텍스트는 같은 엔티티에 대해 같은 인스턴스를 보장
        Assertions.assertThat(user).isSameAs(saveuser);
    }

    @Test
    public void findByUsername_test(){
        String username = "nonUser";
        User user = userRepository.findByUsername(username);
        Assertions.assertThat(user).isNull();
    }
}
