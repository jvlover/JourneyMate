package com.ssafy.journeymate.mateservice.dto.request.docs;


import lombok.Data;

@Data
public class DocsRegistPostReq {

    private String title;
    private Long mateId;
    private String userId;
    private String content;
}
