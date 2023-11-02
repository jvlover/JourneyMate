package com.journeymate.userservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserModifyProfilePutReq {

    String id;

    String nickname;

    String imgUrl;
}
