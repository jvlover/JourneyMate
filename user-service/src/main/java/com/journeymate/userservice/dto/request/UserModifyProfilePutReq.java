package com.journeymate.userservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserModifyProfilePutReq {

    String id;
    
    String nickname;

    String imgUrl;
}
