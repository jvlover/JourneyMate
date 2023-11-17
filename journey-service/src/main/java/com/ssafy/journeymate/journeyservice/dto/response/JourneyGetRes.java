package com.ssafy.journeymate.journeyservice.dto.response;

import lombok.Data;

@Data
public class JourneyGetRes {

    private Long id;
    private Long mateId;
    private Long categoryId;
    private String title;
    private Integer day;
    private Integer sequence;
    private double xcoordinate;
    private double ycoordinate;


}
