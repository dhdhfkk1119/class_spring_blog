package com.tenco.blog.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        
        // JoinDTO 를 User Object 변환 하는 메서드 추가
        // 계층간 데이터 변환을 위해 명확하게 분리
        public User toEntity(){
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
        // 회원가입시 유효성 검증 메서드 추가
        public void validate(){
            if(username == null || username.trim().isEmpty()){
                throw new IllegalArgumentException("사용자 명은 필수야");
            }

            if(password == null || password.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호는 필수야");
            }

            if(!email.contains("@")){
                throw new IllegalArgumentException("이메일 @는 필수야");
            }
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    // 로그인 용 DTO
    public static class LoginDTO{
        private String username;
        private String password;

        // 유혀성 검사
        public void validate() {
            if(username == null || username.trim().isEmpty()){
                throw new IllegalArgumentException("사용자명 입력해");
            }
            if(password == null || password.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호 입력해");
            }
        }

    }

    @Data
    // 로그인 용 DTO
    public static class UpdateDTO{
        private String email;
        private String password;

        // 유혀성 검사
        public void validate() {
            if(email == null || email.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호를 입력해");
            }
            if( password.length() < 4 && password.length() > 13){
                throw new IllegalArgumentException("비밀번호 는 4자 보다 많고 13 자 보다 작아야합니다");
            }
            if(password == null || password.trim().isEmpty()){
                throw new IllegalArgumentException("비밀번호 입력해");
            }
        }

    }
}
