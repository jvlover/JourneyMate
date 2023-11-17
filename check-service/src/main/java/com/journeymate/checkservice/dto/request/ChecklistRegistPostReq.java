package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistRegistPostReq {

    private Long mateId;

    private Long journeyId;

    private List<ItemUpdatePutReq> items;

    @Data
    public static class ItemUpdatePutReq {

        private String name;

        private Integer num;
    }
}
