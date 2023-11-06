package com.ssafy.journeymate.mateservice.dto.response.client;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.Data;


@Data
@JsonSerialize
public class FindUserRes {

    private String message;
    private UserData data;

    @Data
    public static class UserData {

        private String id;
        private String nickname;
        private String imgUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
