package com.ssafy.journeymate.journeyservice.dto.kafka;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    private Long id;
    private Long mate_id;
    private Long category_id;
    private String title;
    private Integer day;
    private Integer sequence;
    private Double xcoordinate;
    private Double ycoordinate;
    private String createdAt;
    private String updatedAt;
    private Integer is_deleted;

}
