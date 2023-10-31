package com.journeymate.checkservice.dto.request;

import com.journeymate.checkservice.dto.response.DefaultChecklistFindRes;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChecklistUpdatePutReq {

    String userid;

    Long journeyId;

    List<DefaultChecklistFindRes> items;
}
