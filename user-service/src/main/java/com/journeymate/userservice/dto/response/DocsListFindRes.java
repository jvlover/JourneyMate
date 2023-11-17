package com.journeymate.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocsListFindRes {

    private String message;

    private DocsListRes data;

    @Data
    @NoArgsConstructor
    public static class DocsListRes {

        private List<DocsListFindData> docsInfoList;

    }

    @Data
    @NoArgsConstructor
    public static class DocsListFindData {

        private String title;

        private Long docsId;

        private String userId;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdDate;

        private List<FileResponseDto> imgFileInfo;

    }

    @Data
    @NoArgsConstructor
    public static class FileResponseDto {

        private String filename;

        private String imgUrl;
    }
}