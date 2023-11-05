package com.journeymate.checkservice.controller;

import com.journeymate.checkservice.dto.ResponseDto;
import com.journeymate.checkservice.dto.request.ChecklistModifyPutReq;
import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.dto.response.ChecklistModifyRes;
import com.journeymate.checkservice.dto.response.ChecklistRegistRes;
import com.journeymate.checkservice.service.ChecklistService;
import java.util.List;
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

    @PostMapping
    public ResponseEntity<ResponseDto> registChecklist(@RequestBody
    ChecklistRegistPostReq checklistRegistPostReq) {

        log.info("ChecklistController_registChecklist_start : " + checklistRegistPostReq);

        List<ChecklistRegistRes> res = checklistService.registChecklist(checklistRegistPostReq);

        log.info("ChecklistController_registChecklist_end : " + res);

        return new ResponseEntity<>(new ResponseDto("체크리스트 저장 완료!", res), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> modifyChecklist(
        @RequestBody ChecklistModifyPutReq checklistModifyPutReq) {

        log.info("ChecklistController_modifyChecklist_start : " + checklistModifyPutReq);

        ChecklistModifyRes res = new ChecklistModifyRes();

        // Todo : 만약에 체크리스트 이미 있으면 다 지우고 다시 저장하기
        return new ResponseEntity<>(new ResponseDto("체크리스트 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/{userId}/{journeyId}")
    public ResponseEntity<ResponseDto> findChecklist(@PathVariable String userId,
        @PathVariable Long journeyId) {

        return new ResponseEntity<>(
            new ResponseDto("체크리스트 반환 완료!",
                checklistService.findChecklistByUserIdAndJourneyId(userId, journeyId)),
            HttpStatus.OK);
    }
}
