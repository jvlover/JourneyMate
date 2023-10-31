package com.journeymate.checkservice.controller;

import com.journeymate.checkservice.dto.ResponseDto;
import com.journeymate.checkservice.dto.request.ChecklistRegistPostReq;
import com.journeymate.checkservice.entity.Checklist;
import com.journeymate.checkservice.service.ChecklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        Checklist res = checklistService.registChecklist(checklistRegistPostReq);

        return new ResponseEntity<>(new ResponseDto("체크리스트 저장 완료!", res), HttpStatus.OK);
    }

    @PutMapping("/exit")
    public ResponseEntity<ResponseDto> deleteUser(@RequestBody String id) {

        return new ResponseEntity<>(new ResponseDto("회원 탈퇴 성공!", true), HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyProfile() {

        return new ResponseEntity<>(new ResponseDto("회원 정보 수정 완료", true), HttpStatus.OK);
    }
}
