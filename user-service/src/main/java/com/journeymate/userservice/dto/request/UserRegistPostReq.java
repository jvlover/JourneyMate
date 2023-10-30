package com.journeymate.userservice.dto.request;

import javax.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRegistPostReq {

    @Email
    String email;

    String nickname;
}
