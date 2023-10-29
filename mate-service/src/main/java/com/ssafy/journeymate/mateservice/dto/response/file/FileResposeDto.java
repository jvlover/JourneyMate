package com.ssafy.journeymate.mateservice.dto.response.file;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResposeDto {

    private String filename;
    private String imgUrl;
}
