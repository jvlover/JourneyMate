package com.ssafy.journeymate.mateservice.dto.response.mate;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MateUpdatePostRes {

    private Long mateId;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> users;
    private String creator;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
