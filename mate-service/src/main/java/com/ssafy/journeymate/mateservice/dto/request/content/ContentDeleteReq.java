package com.ssafy.journeymate.mateservice.dto.request.content;

import java.util.List;
import lombok.Data;

@Data
public class ContentDeleteReq {

    private List<Long> contents;
}
