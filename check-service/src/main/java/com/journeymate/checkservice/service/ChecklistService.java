package com.journeymate.checkservice.service;


import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.response.DefaultChecklistFindRes;
import com.journeymate.checkservice.entity.Checklist;

public interface ChecklistService {

    DefaultChecklistFindRes getDefaultChecklist(Long categoryId);

    Checklist registChecklist(ChecklistRegistPostReq checklistRegistPostReq);

    Checklist getChecklist(Long id);
}
