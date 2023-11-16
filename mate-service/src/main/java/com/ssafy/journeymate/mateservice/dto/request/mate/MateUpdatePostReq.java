package com.ssafy.journeymate.mateservice.dto.request.mate;

import java.util.List;
import lombok.Data;

@Data
public class MateUpdatePostReq {

    private String name;
    private String destination;
    private List<String> users;
    private Long mateId;
}
