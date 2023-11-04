package com.journeymate.checkservice.service;

import com.journeymate.checkservice.client.UserClient;
import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.request.ItemUpdatePutReq;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import com.journeymate.checkservice.dto.response.UserFindRes;
import com.journeymate.checkservice.entity.Checklist;
import com.journeymate.checkservice.exception.ChecklistNotFoundException;
import com.journeymate.checkservice.repository.ChecklistRepository;
import com.journeymate.checkservice.util.BytesHexChanger;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;

    private final BytesHexChanger bytesHexChanger;

    private final UserClient userClient;

    @Autowired
    public ChecklistServiceImpl(ChecklistRepository checklistRepository,
        BytesHexChanger bytesHexChanger, UserClient userClient) {
        this.checklistRepository = checklistRepository;
        this.bytesHexChanger = bytesHexChanger;
        this.userClient = userClient;
    }

    @Override
    public List<ChecklistRegistRes> registChecklist(ChecklistRegistPostReq checklistRegistPostReq) {

        log.info("ChecklistService_registChecklist_start : " + checklistRegistPostReq);

        List<UserFindRes> users = userClient.findUserByMateId(checklistRegistPostReq.getMateId())
            .getData().getUsers();

        List<ChecklistRegistRes> res = new ArrayList<>();

        for (ItemUpdatePutReq item : checklistRegistPostReq.getItems()) {
            for (UserFindRes user : users) {
                Checklist checklist = Checklist.builder()
                    .userId(bytesHexChanger.hexToBytes(user.getId()))
                    .journeyId(checklistRegistPostReq.getJourneyId())
                    .name(item.getName()).num(item.getNum()).isChecked(false).isDeleted(false)
                    .build();

                checklistRepository.save(checklist);

                ChecklistRegistRes checklistRegistRes = new ModelMapper().map(checklist,
                    ChecklistRegistRes.class);

                checklistRegistRes.setUserId(user.getId());

                res.add(checklistRegistRes);
            }
        }

        log.info("ChecklistService_registChecklist_end : " + res);

        return res;
    }

    @Override
    public ChecklistFindRes findChecklistById(Long id) {

        return new ModelMapper().map(
            checklistRepository.findById(id).orElseThrow(ChecklistNotFoundException::new),
            ChecklistFindRes.class);
    }

    @Override
    public List<ChecklistFindRes> findChecklistByuserIdAndJourneyId(String userId, Long journeyId) {

        List<Checklist> checklists = checklistRepository.findChecklistByUserIdAndJourneyId(
            bytesHexChanger.hexToBytes(userId),
            journeyId);
        List<ChecklistFindRes> res = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();
        for (Checklist checklist : checklists) {
            ChecklistFindRes checklistFindRes = modelMapper.map(checklist, ChecklistFindRes.class);
            checklistFindRes.setUserId(bytesHexChanger.bytesToHex(checklist.getUserId()));
            res.add(checklistFindRes);
        }

        return res;
    }

}
