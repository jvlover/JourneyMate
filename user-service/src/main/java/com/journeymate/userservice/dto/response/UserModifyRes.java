package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModifyRes {

    String id;

    String nickname;

    String imgUrl;

    LocalDateTime updatedAt;

}
