package com.ssafy.journeymate.journeyservice.dto.response;

import lombok.Data;

@Data
public class JourneyGetRes {

    private Long id;
    private Long mateId;
    private Integer categoryId;
    private String title;
    private Integer day;
    private Integer sequence;
    private float xcoordinate;
    private float ycoordinate;


}
