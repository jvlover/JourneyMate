package com.ssafy.journeymate.journeyservice.dto.kafka;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    private Long id;
    private Long mate_id;
    private int category_id;
    private String title;
    private int day;
    private int sequence;
    private float xcoordinate;
    private float ycoordinate;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int is_deleted;


}
