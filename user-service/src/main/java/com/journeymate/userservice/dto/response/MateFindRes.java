package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MateFindRes {

    private String message;
    private MateFindData data;

    @Data
    public static class MateFindData {

        private Long mateId;

        private String name;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private List<String> users;

        private String creator;

        private LocalDateTime createdDate;

    }
}
