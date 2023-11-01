package com.ssafy.journeymate.journeyservice.dto.request;

import lombok.Data;

@Data
public class JourneyModifyPutReq {

    private Long journeyId;
    private Long mateId;
    private Long categoryId;
    private String title;
    private Integer day;
    private Integer sequence;
    private float xcoordinate;
    private float ycoordinate;

}
