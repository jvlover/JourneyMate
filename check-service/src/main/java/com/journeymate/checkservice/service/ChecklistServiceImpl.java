package com.journeymate.checkservice.service;

import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.response.DefaultChecklistFindRes;
import com.journeymate.checkservice.entity.Checklist;
import com.journeymate.checkservice.exception.ChecklistNotFoundException;
import com.journeymate.checkservice.repository.ChecklistRepository;
import com.journeymate.checkservice.util.BytesHexChanger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;

    private final BytesHexChanger bytesHexChanger;

    @Autowired
    public ChecklistServiceImpl(ChecklistRepository checklistRepository,
        BytesHexChanger bytesHexChanger) {
        this.checklistRepository = checklistRepository;
        this.bytesHexChanger = bytesHexChanger;
    }


    // feign 으로 처리?
    @Override
    public DefaultChecklistFindRes getDefaultChecklist(Long categoryId) {
        return null;
    }

    @Override
    public Checklist registChecklist(ChecklistRegistPostReq checklistRegistPostReq) {

        Checklist checklist = Checklist.builder().userId(bytesHexChanger.hexToBytes(
            checklistRegistPostReq.getUserId())).name(checklistRegistPostReq.getName()).num(
            checklistRegistPostReq.getNum()).build();

        return checklistRepository.save(checklist);
    }

    @Override
    public Checklist getChecklist(Long id) {
        return checklistRepository.findById(id).orElseThrow(ChecklistNotFoundException::new);
    }
}
