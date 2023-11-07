package com.ssafy.journeymate.mateservice.dto.response.docs;


import com.ssafy.journeymate.mateservice.dto.response.file.FileResposeDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocsListRes {

    private List<DocsInfo> docsInfoList;

    @Data
    @Builder
    public static class DocsInfo {

        private String title;
        private Long docsId;
        private String userId;
        private LocalDateTime createdDate;
        private List<FileResposeDto> imgFileInfo;
    }
}
