package com.journeymate.checkservice.dto.response;

import com.journeymate.checkservice.dto.User;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeFindRes {

    List<User> users;

    String creator;

}
