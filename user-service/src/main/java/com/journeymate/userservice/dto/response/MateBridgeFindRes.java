package com.journeymate.userservice.dto.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeFindRes {

    private List<UserFindRes> users;

    private String creator;

}
