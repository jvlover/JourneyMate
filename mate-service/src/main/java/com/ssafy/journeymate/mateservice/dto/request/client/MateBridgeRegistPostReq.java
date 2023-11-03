package com.ssafy.journeymate.mateservice.dto.request.client;


import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MateBridgeRegistPostReq {

    private Long mateId;
    private List<String> users;
    private String creator;

}
