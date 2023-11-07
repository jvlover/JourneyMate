package com.journeymate.checkservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeFindKafkaRes {

    private String message;

    private MateBridgeFindRes data;

    @Data
    @NoArgsConstructor
    public static class MateBridgeFindRes {

        private List<UserFindRes> users;

        private String creator;

    }

    @Data
    @NoArgsConstructor
    public static class UserFindRes {

        private String id;

        private String nickname;

        private String imgUrl;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

    }
}
