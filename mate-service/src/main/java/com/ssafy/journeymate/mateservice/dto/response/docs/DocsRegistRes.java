package com.ssafy.journeymate.mateservice.dto.response.docs;


import com.ssafy.journeymate.mateservice.dto.response.file.FileResposeDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocsRegistRes {

    private String nickname;
    private String title;
    private String content;
    private Long docsId;
    private LocalDateTime createDate;
    private List<FileResposeDto> imageFileInfo;

}
