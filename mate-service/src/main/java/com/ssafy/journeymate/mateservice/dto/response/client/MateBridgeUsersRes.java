package com.ssafy.journeymate.mateservice.dto.response.client;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonSerialize
public class MateBridgeUsersRes {

    private String message;
    private MateBridgeUser data;

    @Data
    public static class MateBridgeUser{

        private List<User> users;
        private String creator;

        @Data
        public static class User{
            private String id;
            private String email;
            private String nickname;
            private String imgUrl;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
        }

    }
}
