package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserFindRes {

    private String id;

    private String email;

    private String nickname;

    private String imgUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
