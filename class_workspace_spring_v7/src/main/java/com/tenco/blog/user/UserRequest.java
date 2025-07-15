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

        // JoinDTO �� User Object ��ȯ �ϴ� �޼��� �߰�
        // ������ ������ ��ȯ�� ���� ��Ȯ�ϰ� �и�
        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
        // ȸ�����Խ� ��ȿ�� ���� �޼���
        public void validate() {

            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("����� ���� �ʼ���");
            }
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("��й�ȣ�� �ʼ���");
            }
            // ������ �̸��� ���� ���� (����ȭ ǥ����)
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("�ùٸ� �̸��� ������ �ƴմϴ�");
            }
        }
    }
    
    // �α��� �� DTO
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String username; 
        private String password; 
        
        // ��ȿ�� �˻� 
        public void validate() {
            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("�� ����ڸ� �Է���");
            }
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("�� ��й�ȣ �Է���");
            }
        }
        
    }

    // ȸ�� ���� ������ DTO
    @Data
    public static class UpdateDTO {
        private String password;
        private String email;
        // username <-- ����ũ�� ���� ��

        // toEntity (��Ƽüŷ ���)

        public void validate() {
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("��й�ȣ�� �ʼ���");
            }
            if(password.length() < 4) {
                throw new IllegalArgumentException("��й�ȣ�� 4�� �̻��̾�� �մϴ�");
            }
            // ������ �̸��� ���� ���� (����ȭ ǥ����)
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("�ùٸ� �̸��� ������ �ƴմϴ�");
            }
        }

    }
    

}
