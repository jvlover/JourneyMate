package com.ssafy.journeymate.mateservice.dto.response.client;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;

@Data
@JsonSerialize
public class MateBridgeRes implements Serializable {

    private Long id;

    private User user;

    private Long mateId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isCreator;

    @Data
    @Getter
    public class User{
        private String nickname;
    }


}
