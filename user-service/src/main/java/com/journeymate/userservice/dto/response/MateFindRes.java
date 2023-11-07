package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateFindRes {

    private String message;
    private MateFindData data;

    @Data
    @NoArgsConstructor
    public static class MateFindData {

        private Long mateId;

        private String name;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private List<String> users;

        private String destination;

        private String creator;

        private LocalDateTime createdDate;

    }
}
