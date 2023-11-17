package com.journeymate.userservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateBridgeModifyPutReq {

    private Long mateId;

    private List<String> users;

    private String creator;
}
