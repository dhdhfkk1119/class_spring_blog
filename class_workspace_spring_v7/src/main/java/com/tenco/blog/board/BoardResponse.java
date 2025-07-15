package com.tenco.blog.board;

import com.tenco.blog.reply.Reply;
import com.tenco.blog.reply.ReplyResponse;
import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    // 게시글 목록 응답 DTO
    @Data
    public static class MainDTO{
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public MainDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    // 게시글 상세보기 응답 DTO 설계
    @Data
    public static class DetailDTO{
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;
        private boolean isBoardOwner;
        private List<ReplyResponse.ReplyDTO> replyDTOS;

        @Builder
        public DetailDTO(Board board,User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
            this.isBoardOwner = sessionUser != null && board.isOwner(sessionUser.getId());
            this.replyDTOS = new ArrayList<>();
            for(Reply reply : board.getReplies()){
                this.replyDTOS.add(new ReplyResponse.ReplyDTO(reply, sessionUser));
            }
        }
    }

    // 게시물 저장 API
    @Data
    public static class SaveDTO{
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public SaveDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }

    // 게시물 저장 API
    @Data
    public static class UpdateDTO{
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String createdAt;

        @Builder
        public UpdateDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writerName = board.getUser().getUsername();
            this.createdAt = board.getCreatedAt().toString();
        }
    }
}
