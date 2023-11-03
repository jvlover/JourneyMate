package com.journeymate.userservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeRegistPostReq {

    Long mateId;

    List<String> users;

    String creator;

}
