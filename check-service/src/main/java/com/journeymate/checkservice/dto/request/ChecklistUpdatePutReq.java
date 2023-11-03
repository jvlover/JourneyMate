package com.journeymate.checkservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistUpdatePutReq {

    String userId;

    Long journeyId;

    String name;

    Integer num;

    Boolean isChecked;
}
