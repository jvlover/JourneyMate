package com.ssafy.journeymate.mateservice.dto.request.docs;

import lombok.Data;

@Data

public class DocsUpdateReq {

    private String title;
    private String userId;
    private String content;
    private Long docsId;
}
