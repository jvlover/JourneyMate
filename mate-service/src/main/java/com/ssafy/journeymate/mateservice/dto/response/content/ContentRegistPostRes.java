package com.ssafy.journeymate.mateservice.dto.response.content;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
        @JsonSerialize(using =  LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createdDate;
        private String imgUrl;
        private String fileName;
        private Boolean type;
    }
}
