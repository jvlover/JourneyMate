package com.journeymate.userservice.dto.response;

import java.util.List;
import lombok.Data;

@Data
public class MateBridgeFindRes {

    List<UserFindRes> users;

    String creator;

}
