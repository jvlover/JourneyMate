package com.journeymate.userservice.dto.response;

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