package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModifyRes {

    private String id;

    private String nickname;

    private String imgUrl;

    private LocalDateTime updatedAt;

}
