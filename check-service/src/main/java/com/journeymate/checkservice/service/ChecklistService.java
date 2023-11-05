package com.journeymate.checkservice.service;


import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq;
import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistModifyRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import java.util.List;

public interface ChecklistService {

    List<ChecklistRegistRes> registChecklist(ChecklistRegistPostReq checklistRegistPostReq);

    ChecklistFindRes findChecklistById(Long id);

    ChecklistModifyRes modifyChecklist(ChecklistModifyPutReq checklistModifyPutReq);

    List<ChecklistFindRes> findChecklistByUserIdAndJourneyId(String userId, Long journeyId);
}
