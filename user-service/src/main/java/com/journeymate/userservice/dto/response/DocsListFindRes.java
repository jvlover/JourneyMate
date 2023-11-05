package com.journeymate.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocsListFindRes {

    private String message;
    private List<DocsListFindData> data;

    @Data
    public static class DocsListFindData {

        private String title;

        private Long docsId;

        private LocalDateTime createdDate;

        private List<FileFindRes> imgFileInfo;

    }

    @Data
    public static class FileFindRes {

        private String filename;
        
        private String imgUrl;
    }
}