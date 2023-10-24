package com.journeymate.userservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MateBridgeRegistPostReq {

    Long mateId;

    List<String> users;

    String creator;

}
