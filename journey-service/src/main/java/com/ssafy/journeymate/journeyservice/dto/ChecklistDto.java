package com.ssafy.journeymate.journeyservice.dto;

import com.ssafy.journeymate.journeyservice.dto.response.ItemGetRes;
import java.util.List;
import lombok.Data;


@Data
public class ChecklistDto {

    Long mateId;
    Long journeyId;
    String type;
    List<ItemGetRes> items;

    public ChecklistDto(Long mateId, Long journeyId, String type, List<ItemGetRes> items) {
        this.mateId = mateId;
        this.journeyId = journeyId;
        this.type = type;
        this.items = items;
    }
}
