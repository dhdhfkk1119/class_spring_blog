package com.tenco.blog.reply;

import com.tenco.blog.user.User;
import lombok.Builder;
import lombok.Data;

public class ReplyResponse {

    @Data
    public static class ReplyDTO{
        private Long id;
        private String comment;
        private String writerName;
        private String createdAt;
        private boolean isReplyOwner;

        @Builder
        public ReplyDTO(Reply reply, User sessionUser) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.writerName = reply.getUser().getUsername();
            this.createdAt = reply.getCreatedAt().toString();
            this.isReplyOwner = sessionUser != null && reply.isOwner(sessionUser.getId());
        }
    }
}
