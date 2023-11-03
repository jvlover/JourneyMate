package com.journeymate.checkservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistRegistRes {

    private String id;

    private String userId;

    private Long journeyId;

    private String name;

    private Integer num;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
