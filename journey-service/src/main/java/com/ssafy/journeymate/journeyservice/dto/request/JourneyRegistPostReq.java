package com.ssafy.journeymate.journeyservice.dto.request;

import lombok.Data;

@Data
public class JourneyRegistPostReq {

    private Long mateId;
    private Long categoryId;
    private String title;
    private Integer day;
    private Integer sequence;
    private double xcoordinate;
    private double ycoordinate;

}
