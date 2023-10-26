package com.ssafy.journeymate.mateservice.dto.request.mate;

import lombok.Data;

import java.util.List;

@Data
public class MateUpdatePostReq {

    private String name;
    private String destination;
    private List<String> users;
    private Long mateId;
}
