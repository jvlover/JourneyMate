package com.journeymate.userservice.dto.response;

import com.journeymate.userservice.entity.User;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeFindRes {

    List<User> users;

    String creator;

}
