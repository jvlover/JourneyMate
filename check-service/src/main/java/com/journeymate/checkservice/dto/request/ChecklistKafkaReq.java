package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistKafkaReq {

    Long mateId;

    Long journeyId;

    List<DefaultItem> defaultItems;

    @Data
    public class DefaultItem {

        String name;

        Integer num;
    }
}
