package com.journeymate.userservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModifyProfilePutReq {

    private String id;

    private String nickname;

    private String imgUrl;
}
