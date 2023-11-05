package com.ssafy.journeymate.mateservice.dto.response.client;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MateBridgeRes {

    private String message;
    private List<MateBridgeData> data;

    @Data
    public static class MateBridgeData {

        private int id;
        private User user;
        private Long mateId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;
        private Boolean isCreator;


        @Data
        public static class User {

            private String id;
            private String email;
            private String nickname;
            private String imgUrl;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
            private Boolean isDeleted;
        }

    }
}
