package com.ssafy.journeymate.mateservice.dto.request.client;

import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MateBridgeModifyReq {

    private Long mateId;
    private List<String> user;
    private String creator;

}
