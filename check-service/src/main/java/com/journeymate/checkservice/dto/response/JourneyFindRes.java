package com.journeymate.checkservice.dto.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JourneyFindRes {

    private String message;
    private List<JourneyFindData> data;

    @Data
    @NoArgsConstructor
    public static class JourneyFindData {

        private Long id;
        private Long mateId;
        private Long categoryId;
        private String title;
        private Integer day;
        private Integer sequence;
        private double xcoordinate;
        private double ycoordinate;

    }
}
