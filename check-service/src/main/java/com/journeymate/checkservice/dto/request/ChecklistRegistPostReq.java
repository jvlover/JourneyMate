package com.journeymate.checkservice.dto.request;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistRegistPostReq {

    Long mateId;

    Long journeyId;

    List<ItemUpdatePutReq> items;
}
