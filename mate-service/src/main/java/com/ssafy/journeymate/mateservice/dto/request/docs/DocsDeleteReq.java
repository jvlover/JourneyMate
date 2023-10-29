package com.ssafy.journeymate.mateservice.dto.request.docs;


import lombok.Data;

@Data
public class DocsDeleteReq {

    private Long docsId;
    private Long userId;

}
