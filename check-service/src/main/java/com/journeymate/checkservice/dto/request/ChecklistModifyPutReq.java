package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistModifyPutReq {

    String userId;

    Long journeyId;

    List<Item> items;

    @Data
    public class Item {

        Long id;
        
        String name;

        Integer num;

        Boolean isChecked;

        Boolean isDeleted;

    }
}
