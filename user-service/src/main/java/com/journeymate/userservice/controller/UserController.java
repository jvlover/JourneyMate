package com.journeymate.userservice.controller;

import com.journeymate.userservice.dto.ResponseDto;
import com.journeymate.userservice.dto.request.MateBridgeModifyPutReq;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.MateBridgeModifyRes;
import com.journeymate.userservice.dto.response.MateBridgeRegistRes;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.dto.response.UserRegistRes;
import com.journeymate.userservice.service.MateBridgeService;
import com.journeymate.userservice.service.UserService;
import com.journeymate.userservice.util.BytesHexChanger;
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

    private final BytesHexChanger bytesHexChanger;

    @Autowired
    public UserController(UserService userService, MateBridgeService mateBridgeService,
        BytesHexChanger bytesHexChanger) {
        this.userService = userService;
        this.mateBridgeService = mateBridgeService;
        this.bytesHexChanger = bytesHexChanger;
    }

    @PostMapping("/regist")
    @Transactional
    public ResponseEntity<ResponseDto> socialLogin(
        @RequestBody UserRegistPostReq userRegistPostReq) {

        byte[] hexId = userService.createUUID();

        // TODO: 회원 있으면 login 없으면 registUser
        UserRegistRes res = userService.registUser(hexId, userRegistPostReq.getNickname());

        return new ResponseEntity<>(new ResponseDto("로그인 완료!", res), HttpStatus.OK);
    }

    @PostMapping("/mateBridge")
    @Transactional
    public ResponseEntity<ResponseDto> registMateBridge(@RequestBody
    MateBridgeRegistPostReq mateBridgeRegistPostReq) {

        List<MateBridgeRegistRes> res = mateBridgeService.registMateBridge(mateBridgeRegistPostReq);

        return new ResponseEntity<>(new ResponseDto("메이트 브릿지 저장 완료!", res), HttpStatus.OK);
    }

    @PutMapping("/mateBridge")
    @Transactional
    public ResponseEntity<ResponseDto> modifyMateBridge(
        @RequestBody MateBridgeModifyPutReq mateBridgeModifyPutReq) {

        List<MateBridgeModifyRes> res = mateBridgeService.modifyMateBridge(mateBridgeModifyPutReq);

        return new ResponseEntity<>(new ResponseDto("메이트 브릿지 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseDto> findUserById(@PathVariable String id) {

        UserFindRes res = userService.findUserById(bytesHexChanger.hexToBytes(id));

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/findByNickname/{nickname}")
    public ResponseEntity<ResponseDto> findUserByNickname(@PathVariable String nickname) {

        UserFindRes res = userService.findUserByNickname(nickname);

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

        userService.deleteUser(bytesHexChanger.hexToBytes(id));

        return new ResponseEntity<>(new ResponseDto("회원 탈퇴 성공!", true), HttpStatus.OK);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ResponseDto> modifyProfile(
        UserModifyProfilePutReq userModifyProfilePutReq) {

        UserModifyRes res = userService.modifyProfile(userModifyProfilePutReq);

        return new ResponseEntity<>(new ResponseDto("회원 정보 수정 완료", res), HttpStatus.OK);
    }

    @GetMapping("/duplicateCheck/{nickname}")
    @Transactional
    public ResponseEntity<ResponseDto> nicknameDuplicateCheck(@PathVariable String nickname) {

        Boolean nicknameDuplicate = userService.nicknameDuplicateCheck(nickname);

        return new ResponseEntity<>(new ResponseDto("닉네임 중복 확인", nicknameDuplicate), HttpStatus.OK);
    }

    @GetMapping("/mate/{id}")
    @Transactional
    public ResponseEntity<ResponseDto> findMateById(@PathVariable String id) {

        return new ResponseEntity<>(new ResponseDto("그룹 확인", userService.findMateById(id)),
            HttpStatus.OK);
    }
}
