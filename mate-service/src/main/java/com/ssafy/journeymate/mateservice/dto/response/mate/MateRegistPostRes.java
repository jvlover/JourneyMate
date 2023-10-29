package com.ssafy.journeymate.mateservice.dto.response.mate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class MateRegistPostRes {

    private String name;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> users;
    private String creator;
    private LocalDateTime createdDate;
    private Long mateId;
}
