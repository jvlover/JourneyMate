package com.ssafy.journeymate.journeyservice.dto.request;

import lombok.Data;

@Data
public class JourneyModifyReq {

    private Integer categoryId;
    private String title;
    private float xcoordinate;
    private float ycoordinate;

}
