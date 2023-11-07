package com.ssafy.journeymate.mateservice.dto.response.content;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentRegistPostRes {

    List<content> contentInfo;

    @Data
    @Builder
    public static class content {

        private String creatorId;
        private Long contentId;
        private LocalDateTime createdDate;
        private String imgUrl;
        private String fileName;
        private Boolean type;
    }
}
