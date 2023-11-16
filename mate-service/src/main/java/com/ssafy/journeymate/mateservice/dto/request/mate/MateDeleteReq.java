package com.ssafy.journeymate.mateservice.dto.request.mate;


import lombok.Data;

@Data
public class MateDeleteReq {

    private Long mateId;
    private String creator;
}
