package com.journeymate.checkservice.service;


import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import java.util.List;

public interface ChecklistService {

    List<ChecklistRegistRes> registChecklist(ChecklistRegistPostReq checklistRegistPostReq);

    ChecklistFindRes findChecklistById(Long id);

    List<ChecklistFindRes> findChecklistByuserIdAndJourneyId(String userId, Long journeyId);
}
