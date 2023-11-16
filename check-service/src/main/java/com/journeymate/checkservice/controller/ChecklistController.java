package com.journeymate.checkservice.controller;

import com.journeymate.checkservice.dto.ResponseDto;
import com.journeymate.checkservice.dto.request.ChecklistKafkaReq;
import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq;
import com.journeymate.checkservice.dto.response.ChecklistFindRes;
import com.journeymate.checkservice.dto.response.ChecklistModifyRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import com.journeymate.checkservice.service.ChecklistService;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/check-service")
public class ChecklistController {

    private final ChecklistService checklistService;

    @Autowired
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PostMapping("/kafka")
    @Transactional
    public ResponseEntity<ResponseDto> registChecklist(@RequestBody
    ChecklistKafkaReq checklistKafkaReq) {

        log.info("ChecklistController_registChecklist_start : " + checklistKafkaReq);

        List<ChecklistRegistRes> res = checklistService.registChecklist(checklistKafkaReq);

        log.info("ChecklistController_registChecklist_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 저장 완료!", res), HttpStatus.OK);
    }

    @PutMapping("/kafka/{journeyId}")
    @Transactional
    public ResponseEntity<ResponseDto> deleteChecklist(@PathVariable Long journeyId) {

        log.info("ChecklistController_deleteChecklist_start : " + journeyId);

        checklistService.deleteChecklist(journeyId);

        log.info("ChecklistController_deleteChecklist_end : SUCCESS");

        return new ResponseEntity<>(new ResponseDto("체크리스트 삭제 완료!", true), HttpStatus.OK);
    }

    @PutMapping("/kafka")
    @Transactional
    public ResponseEntity<ResponseDto> updateChecklist(@RequestBody
    ChecklistKafkaReq checklistKafkaReq) {

        log.info("ChecklistController_updateChecklist_start : " + checklistKafkaReq);

        List<ChecklistRegistRes> res = checklistService.updateChecklist(checklistKafkaReq);

        log.info("ChecklistController_updateChecklist_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 수정 완료!", res), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findChecklistById(@PathVariable Long id) {

        log.info("ChecklistController_findChecklist_start : " + id);

        ChecklistFindRes res = checklistService.findChecklistById(id);

        log.info("ChecklistController_findChecklist_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 반환 완료!", res), HttpStatus.OK);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ResponseDto> modifyPersonalChecklist(
        @RequestBody ChecklistModifyPutReq checklistModifyPutReq) {

        log.info("ChecklistController_modifyPersonalChecklist_start : " + checklistModifyPutReq);

        List<ChecklistModifyRes> res = checklistService.modifyPersonalChecklist(
            checklistModifyPutReq);

        log.info("ChecklistController_modifyPersonalChecklist_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/{userId}/{journeyId}")
    public ResponseEntity<ResponseDto> findChecklistByUserIdAndJourneyId(
        @PathVariable String userId,
        @PathVariable Long journeyId) {

        log.info("ChecklistController_findChecklistByUserIdAndJourneyId_start : " + userId + " "
            + journeyId);

        List<ChecklistFindRes> res = checklistService.findChecklistByUserIdAndJourneyId(userId,
            journeyId);

        log.info("ChecklistController_findChecklistByUserIdAndJourneyId_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 반환 완료!", res), HttpStatus.OK);
    }

    @GetMapping("/mate/{userId}/{mateId}")
    public ResponseEntity<ResponseDto> findChecklistByUserIdAndMateId(@PathVariable String userId,
        @PathVariable Long mateId) {

        log.info("ChecklistController_findChecklistByUserIdAndMateyId_start : " + userId + " "
            + mateId);

        List<ChecklistFindRes> res = checklistService.findChecklistByUserIdAndMateId(userId,
            mateId);

        log.info("ChecklistController_findChecklistByUserIdAndMateId_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 반환 완료!", res), HttpStatus.OK);
    }
}
