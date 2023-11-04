package com.ssafy.journeymate.mateservice.dto.response.client;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@JsonSerialize
public class FindUserRes{

    private String message;
    private UserData data;

    @Data
    public static class UserData{
        private String id;
        private String nickname;
        private String imgUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}
