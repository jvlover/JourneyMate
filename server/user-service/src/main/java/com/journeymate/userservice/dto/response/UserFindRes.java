package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFindRes {

    private String id;

    private String email;

    private String nickname;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String accessToken;

    private String refreshToken;
}
