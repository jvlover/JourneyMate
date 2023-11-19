package com.journeymate.checkservice.service;

import com.journeymate.checkservice.client.JourneyClient;
import com.journeymate.checkservice.client.UserClient;
import com.journeymate.checkservice.dto.request.ChecklistKafkaReq;
import com.journeymate.checkservice.dto.request.ChecklistKafkaReq.Item;
import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq;
import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq.PersonalItem;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistModifyRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import com.journeymate.checkservice.dto.response.JourneyFindRes;
import com.journeymate.checkservice.dto.response.JourneyFindRes.JourneyFindData;
import com.journeymate.checkservice.dto.response.MateBridgeFindKafkaRes.UserFindRes;
import com.journeymate.checkservice.entity.Checklist;
import com.journeymate.checkservice.exception.ChecklistNotFoundException;
import com.journeymate.checkservice.repository.ChecklistRepository;
import com.journeymate.checkservice.util.BytesHexChanger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final UserClient userClient;
    private final JourneyClient journeyClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    private final BytesHexChanger bytesHexChanger = new BytesHexChanger();
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ChecklistServiceImpl(ChecklistRepository checklistRepository, UserClient userClient,
        JourneyClient journeyClient, CircuitBreakerFactory circuitBreakerFactory) {

        this.checklistRepository = checklistRepository;

        this.userClient = userClient;
        this.journeyClient = journeyClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public List<ChecklistRegistRes> registChecklist(ChecklistKafkaReq checklistKafkaReq) {

        log.info("ChecklistService_registChecklist_start : " + checklistKafkaReq);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-find-circuitbreaker");

        List<UserFindRes> users = circuitBreaker.run(
            () -> userClient.findUserByMateId(checklistKafkaReq.getMateId())
                .getData().getUsers(), throwable -> new ArrayList<>());

        System.out.println(users + "이건 유저");

        List<ChecklistRegistRes> res = new ArrayList<>();

        for (Item item : checklistKafkaReq.getItems()) {

            System.out.println(item + "이거임");

            for (UserFindRes user : users) {

                Checklist checklist = Checklist.builder()
                    .userId(bytesHexChanger.hexToBytes(user.getId()))
                    .journeyId(checklistKafkaReq.getJourneyId())
                    .name(item.getName()).num(item.getNum()).isChecked(false)
                    .isDeleted(false)
                    .build();

                checklistRepository.save(checklist);

                ChecklistRegistRes checklistRegistRes = modelMapper.map(checklist,
                    ChecklistRegistRes.class);

                checklistRegistRes.setUserId(user.getId());

                res.add(checklistRegistRes);
            }
        }

        log.info("ChecklistService_registChecklist_end : " + res);

        return res;
    }

    @Override
    public void deleteChecklist(Long journeyId) {

        log.info("ChecklistService_deleteChecklist_start : " + journeyId);

        List<Checklist> checklists = checklistRepository.findChecklistByJourneyId(journeyId);

        for (Checklist checklist : checklists) {

            checklist.deleteChecklist();

        }

        log.info("ChecklistService_deleteChecklist_end : SUCCESS");
    }

    @Override
    public List<ChecklistRegistRes> updateChecklist(ChecklistKafkaReq checklistKafkaReq) {

        log.info("ChecklistService_updateChecklist_start : " + checklistKafkaReq);

        List<Checklist> checklists = checklistRepository.findChecklistByJourneyId(
            checklistKafkaReq.getJourneyId());

        for (Checklist checklist : checklists) {

            checklist.deleteChecklist();

        }

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-find-circuitbreaker");

        List<UserFindRes> users = circuitBreaker.run(
            () -> userClient.findUserByMateId(checklistKafkaReq.getMateId())
                .getData().getUsers(), throwable -> new ArrayList<>());

        List<ChecklistRegistRes> res = new ArrayList<>();

        for (Item item : checklistKafkaReq.getItems()) {

            for (UserFindRes user : users) {

                Checklist checklist = Checklist.builder()
                    .userId(bytesHexChanger.hexToBytes(user.getId()))
                    .journeyId(checklistKafkaReq.getJourneyId())
                    .name(item.getName()).num(item.getNum()).isChecked(false)
                    .isDeleted(false)
                    .build();

                checklistRepository.save(checklist);

                ChecklistRegistRes checklistRegistRes = modelMapper.map(checklist,
                    ChecklistRegistRes.class);

                checklistRegistRes.setUserId(user.getId());

                res.add(checklistRegistRes);
            }
        }

        log.info("ChecklistService_updateChecklist_end : " + res);

        return res;
    }


    @Override
    public ChecklistFindRes findChecklistById(Long id) {

        log.info("ChecklistService_findChecklistById_start : " + id);

        Checklist checklist = checklistRepository.findById(id)
            .orElseThrow(ChecklistNotFoundException::new);

        ChecklistFindRes res = modelMapper.map(
            checklist,
            ChecklistFindRes.class);

        res.setUserId(bytesHexChanger.bytesToHex(checklist.getUserId()));

        log.info("ChecklistService_findChecklistById_end : " + res);

        return res;
    }

    @Override
    public List<ChecklistModifyRes> modifyPersonalChecklist(
        ChecklistModifyPutReq checklistModifyPutReq) {

        log.info("ChecklistService_modifyChecklist_start : " + checklistModifyPutReq);

        // 기존 체크리스트 
        List<Checklist> checklists = checklistRepository.findChecklistByUserIdAndJourneyId(
            bytesHexChanger.hexToBytes(checklistModifyPutReq.getUserId()),
            checklistModifyPutReq.getJourneyId());

        // 새롭게 추가된 체크리스트
        List<PersonalItem> personalItems = checklistModifyPutReq.getPersonalItems();

        List<ChecklistModifyRes> res = new ArrayList<>();

        HashMap<Long, PersonalItem> map = new HashMap<>();

        for (PersonalItem personalItem : personalItems) {

            // 새롭게 추가된 item의 경우
            if (personalItem.getId() == 0) {

                Checklist checklist = Checklist.builder()
                    .userId(bytesHexChanger.hexToBytes(checklistModifyPutReq.getUserId()))
                    .journeyId(checklistModifyPutReq.getJourneyId())
                    .name(personalItem.getName()).num(personalItem.getNum())
                    .isChecked(personalItem.getIsChecked())
                    .isDeleted(personalItem.getIsDeleted())
                    .build();

                checklistRepository.save(checklist);

                ChecklistModifyRes checklistModifyRes = modelMapper.map(checklist,
                    ChecklistModifyRes.class);

                checklistModifyRes.setUserId(checklistModifyPutReq.getUserId());

                res.add(checklistModifyRes);

            } else {

                map.put(personalItem.getId(), personalItem);

            }

        }

        for (Checklist checklist : checklists) {

            if (map.containsKey(checklist.getId())) {

                PersonalItem personalItem = map.get(checklist.getId());

                if (!(personalItem.getNum() == checklist.getNum() && personalItem.getName()
                    .equals(checklist.getNum())
                    && personalItem.getIsChecked() == checklist.getIsChecked())) {

                    checklist.modifyChecklist(personalItem.getName(), personalItem.getNum(),
                        personalItem.getIsChecked());

                }

                ChecklistModifyRes checklistModifyRes = modelMapper.map(checklist,
                    ChecklistModifyRes.class);

                checklistModifyRes.setUserId(checklistModifyPutReq.getUserId());

                res.add(checklistModifyRes);

            } else {

                // 수정된 체크리스트에 없다면 삭제
                checklist.deleteChecklist();

            }
        }

        log.info("ChecklistService_modifyChecklist_end : " + res);

        return res;
    }

    @Override
    public List<ChecklistFindRes> findChecklistByUserIdAndJourneyId(String userId, Long journeyId) {

        log.info(
            "ChecklistService_findChecklistByUserIdAndJourneyId_start : " + userId + " "
                + journeyId);

        List<Checklist> checklists = checklistRepository.findChecklistByUserIdAndJourneyId(
            bytesHexChanger.hexToBytes(userId),
            journeyId);

        List<ChecklistFindRes> res = new ArrayList<>();

        for (Checklist checklist : checklists) {

            ChecklistFindRes checklistFindRes = modelMapper.map(checklist, ChecklistFindRes.class);

            checklistFindRes.setUserId(bytesHexChanger.bytesToHex(checklist.getUserId()));

            res.add(checklistFindRes);
        }

        log.info("ChecklistService_findChecklistByUserIdAndJourneyId_end : " + res);

        return res;
    }

    @Override
    public List<ChecklistFindRes> findChecklistByUserIdAndMateId(String userId, Long mateId) {

        log.info(
            "ChecklistService_findChecklistByUserIdAndMateId_start : " + userId + " "
                + mateId);

        List<ChecklistFindRes> res = new ArrayList<>();
        List<Checklist> checklists = new ArrayList<>();

        JourneyFindRes journeyFindReses = journeyClient.findJourneyByMateId(mateId);

        log.info("ChecklistService_findChecklistByUserIdAndMateId : " + journeyFindReses);

        for (JourneyFindData journeyFinddata : journeyFindReses.getData()) {

            checklists.addAll(checklistRepository.findChecklistByUserIdAndJourneyId(
                bytesHexChanger.hexToBytes(userId),
                journeyFinddata.getId()));

        }

        for (Checklist checklist : checklists) {

            ChecklistFindRes checklistFindRes = modelMapper.map(checklist, ChecklistFindRes.class);

            checklistFindRes.setUserId(bytesHexChanger.bytesToHex(checklist.getUserId()));

            res.add(checklistFindRes);
        }

        log.info("ChecklistService_findChecklistByUserIdAndMateId_end : " + res);

        return res;
    }
}
