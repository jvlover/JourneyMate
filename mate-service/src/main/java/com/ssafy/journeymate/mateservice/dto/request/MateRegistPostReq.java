package com.ssafy.journeymate.mateservice.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Data
public class MateRegistPostReq {

    private String name;
    private String destination;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> users;
    private String creator;

}
