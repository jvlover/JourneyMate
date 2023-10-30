package com.ssafy.journeymate.mateservice.dto.response.content;


import com.ssafy.journeymate.mateservice.dto.response.file.FileResposeDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ContentRegistPostRes {

    List<content> contentInfo;


    @Data
    @Builder
    @Getter
    public static class content {

        private String creatorId;
        private Long contentId;
        private LocalDateTime createdDate;
        private String imgUrl;
        private String fileName;
    }

}
