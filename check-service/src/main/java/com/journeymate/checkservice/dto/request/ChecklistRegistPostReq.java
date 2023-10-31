package com.journeymate.checkservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChecklistRegistPostReq {

    String userId;

    Long journeyId;

    String name;

    Integer num;

}
