package com.journeymate.checkservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemUpdatePutReq {

    String name;

    Integer num;
}
