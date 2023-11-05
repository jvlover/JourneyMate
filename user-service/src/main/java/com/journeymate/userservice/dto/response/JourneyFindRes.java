package com.journeymate.userservice.dto.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JourneyFindRes {

    private String message;

    private List<JourneyFindData> data;

    @Data
    public static class JourneyFindData {

        private Long id;

        private Long mateId;

        private Long categoryId;

        private String title;

        private Integer day;

        private Integer sequence;

        private float xcoordinate;

        private float ycoordinate;
    }

}
