package com.ssafy.journeymate.mateservice.dto.response.mate;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
