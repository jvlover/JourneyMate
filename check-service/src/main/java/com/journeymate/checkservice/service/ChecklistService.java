package com.journeymate.checkservice.service;


import com.journeymate.checkservice.dto.request.ChecklistKafkaReq;
import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistModifyRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import java.util.List;

public interface ChecklistService {

    List<ChecklistRegistRes> registChecklist(ChecklistKafkaReq checklistKafkaReq);

    void deleteChecklist(Long journeyId);

    List<ChecklistRegistRes> updateChecklist(ChecklistKafkaReq checklistKafkaReq);

    ChecklistFindRes findChecklistById(Long id);

    List<ChecklistModifyRes> modifyPersonalChecklist(ChecklistModifyPutReq checklistModifyPutReq);

    List<ChecklistFindRes> findChecklistByUserIdAndJourneyId(String userId, Long journeyId);

    List<ChecklistFindRes> findChecklistByUserIdAndMateId(String userId, Long mateId);
}
