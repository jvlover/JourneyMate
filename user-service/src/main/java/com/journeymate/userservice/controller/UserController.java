package com.journeymate.userservice.controller;

import com.journeymate.userservice.dto.ResponseDto;
import com.journeymate.userservice.dto.request.MateBridgeModifyPutReq;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.DocsListFindRes.DocsListFindData;
import com.journeymate.userservice.dto.response.JourneyFindRes.JourneyFindData;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.MateBridgeModifyRes;
import com.journeymate.userservice.dto.response.MateBridgeRegistRes;
import com.journeymate.userservice.dto.response.MateFindRes.MateFindData;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.dto.response.UserRegistRes;
import com.journeymate.userservice.service.MateBridgeService;
import com.journeymate.userservice.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;
    private final MateBridgeService mateBridgeService;

    @Autowired
    public UserController(UserService userService, MateBridgeService mateBridgeService) {
        this.userService = userService;
        this.mateBridgeService = mateBridgeService;
    }

    @PostMapping("/regist")
    @Transactional
    public ResponseEntity<ResponseDto> socialLogin(
        @RequestBody UserRegistPostReq userRegistPostReq) {

        log.info("UserController_socialLogin_start : " + userRegistPostReq);

        // TODO: 회원 있으면 login 없으면 registUser
        UserRegistRes res = userService.registUser(userRegistPostReq);

        log.info("UserController_socialLogin_end : " + res);

        return new ResponseEntity<>(new ResponseDto("로그인 완료!", res), HttpStatus.OK);
    }

    @PostMapping("/mateBridge")
    @Transactional
    public ResponseEntity<ResponseDto> registMateBridge(@RequestBody
    MateBridgeRegistPostReq mateBridgeRegistPostReq) {

        log.info("UserController_registMateBridge_start : " + mateBridgeRegistPostReq);

        List<MateBridgeRegistRes> res = mateBridgeService.registMateBridge(mateBridgeRegistPostReq);

        log.info("UserController_registMateBridge_end : " + res);

        return new ResponseEntity<>(new ResponseDto("메이트 브릿지 저장 완료!", res), HttpStatus.OK);
    }

    @PutMapping("/mateBridge")
    @Transactional
    public ResponseEntity<ResponseDto> modifyMateBridge(
        @RequestBody MateBridgeModifyPutReq mateBridgeModifyPutReq) {

        log.info("UserController_modifyMateBridge_start : " + mateBridgeModifyPutReq);

        List<MateBridgeModifyRes> res = mateBridgeService.modifyMateBridge(mateBridgeModifyPutReq);

        log.info("UserController_modifyMateBridge_end : " + res);

        return new ResponseEntity<>(new ResponseDto("메이트 브릿지 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseDto> findUserById(@PathVariable String id) {

        log.info("UserController_findUserById_start : " + id);

        UserFindRes res = userService.findUserById(id);

        log.info("UserController_findUserById_end : " + res);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/findByNickname/{nickname}")
    public ResponseEntity<ResponseDto> findUserByNickname(@PathVariable String nickname) {

        log.info("UserController_findUserByNickname_start : " + nickname);

        UserFindRes res = userService.findUserByNickname(nickname);

        log.info("UserController_findUserByNickname_end : " + res);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/mateBridge/{mateId}")
    public ResponseEntity<ResponseDto> findUserByMateId(@PathVariable Long mateId) {

        log.info("UserController_findUserByMateId_start : " + mateId);

        MateBridgeFindRes res = mateBridgeService.findMateBridgeByMateId(mateId);

        log.info("UserController_findUserByMateId_end : " + res.toString());

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @PutMapping("/exit/{id}")
    @Transactional
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable String id) {

        log.info("UserController_deleteUser_start : " + id);

        userService.deleteUser(id);

        log.info("UserController_deleteUser_end : SUCCESS");

        return new ResponseEntity<>(new ResponseDto("회원 탈퇴 성공!", true), HttpStatus.OK);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ResponseDto> modifyProfile(
        @RequestBody UserModifyProfilePutReq userModifyProfilePutReq) {

        log.info("UserController_modifyProfile_end : " + userModifyProfilePutReq);

        UserModifyRes res = userService.modifyProfile(userModifyProfilePutReq);

        log.info("UserController_mmodifyProfile_end : " + res);

        return new ResponseEntity<>(new ResponseDto("회원 정보 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/duplicateCheck/{nickname}")
    public ResponseEntity<ResponseDto> nicknameDuplicateCheck(@PathVariable String nickname) {

        log.info("UserController_nicknameDuplicateCheck_start : " + nickname);

        Boolean res = userService.nicknameDuplicateCheck(nickname);

        log.info("UserController_nicknameDuplicateCheck_end : " + res);

        return new ResponseEntity<>(new ResponseDto("닉네임 중복 확인", res), HttpStatus.OK);
    }

    @GetMapping("/mate/{id}")
    public ResponseEntity<ResponseDto> findMateById(@PathVariable String id) {

        log.info("UserController_findMateById_start : " + id);

        List<MateFindData> res = userService.findMateById(id);

        log.info("UserController_findMateById_end : " + res);

        return new ResponseEntity<>(new ResponseDto("그룹 확인", res),
            HttpStatus.OK);
    }

    @GetMapping("/journey/{id}")
    public ResponseEntity<ResponseDto> findTodayJourneyById(@PathVariable String id) {

        log.info("UserController_findTodayJourneyById_start : " + id);

        List<JourneyFindData> res = userService.findTodayJourneyById(id);

        log.info("UserController_findTodayJourneyById_end : " + res);

        return new ResponseEntity<>(new ResponseDto("오늘의 일정 조회 완료!", res), HttpStatus.OK);
    }

    @GetMapping("/docs/{id}")
    public ResponseEntity<ResponseDto> findDocsById(@PathVariable String id) {

        log.info("UserController_findDocsById_start : " + id);

        List<DocsListFindData> res = userService.findDocsById(id);

        log.info("UserController_findDocsById_end : " + res);

        return new ResponseEntity<>(new ResponseDto("문서 조회 완료!", res), HttpStatus.OK);
    }
}
