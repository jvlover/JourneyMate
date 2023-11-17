package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistKafkaReq {

    private Long mateId;

    private Long journeyId;

    private List<Item> items;

    @Data
    public static class Item {

        private String name;

        private Integer num;
    }
}
