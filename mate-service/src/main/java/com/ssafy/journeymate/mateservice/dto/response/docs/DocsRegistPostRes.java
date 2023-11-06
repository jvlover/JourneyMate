package com.ssafy.journeymate.mateservice.dto.response.docs;


import com.ssafy.journeymate.mateservice.dto.response.file.FileResposeDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocsRegistPostRes {

    private String nickname;
    private String title;
    private String content;
    private Long docsId;
    private LocalDateTime createdDate;
    private List<FileResposeDto> imgFileInfo;
}
