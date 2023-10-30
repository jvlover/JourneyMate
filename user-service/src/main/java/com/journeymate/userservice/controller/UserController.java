package com.journeymate.userservice.controller;

import com.journeymate.userservice.dto.ResponseDto;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.entity.MateBridge;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.service.MateBridgeService;
import com.journeymate.userservice.service.UserService;
import com.journeymate.userservice.util.BytesHexChanger;
import java.util.ArrayList;
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
    public ResponseEntity<ResponseDto> socialLogin(
        @RequestBody UserRegistPostReq userRegistPostReq) {

        byte[] hexId = userService.createUUID();

        // TODO: 회원 있으면 login 없으면 registUser
        User res = userService.registUser(hexId, userRegistPostReq.getEmail(),
            userRegistPostReq.getNickname());

        return new ResponseEntity<>(new ResponseDto("로그인 완료!", res), HttpStatus.OK);
    }

    @PostMapping("/mateBridge")
    public ResponseEntity<ResponseDto> registMateBridge(@RequestBody
    MateBridgeRegistPostReq mateBridgeRegistPostReq) {

        List<MateBridge> res = mateBridgeService.registMateBridge(mateBridgeRegistPostReq);

        return new ResponseEntity<>(new ResponseDto("메이트 멤버 저장 완료!", res), HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseDto> findUserById(@PathVariable String id) {

        UserFindRes res = userService.FindUserById(bytesHexChanger.hexToBytes(id));

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }


    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<ResponseDto> findUserByEmail(@PathVariable String email) {

        UserFindRes res = userService.FindUserByEmail(email);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/findByNickname/{nickname}")
    public ResponseEntity<ResponseDto> findUserByNickname(@PathVariable String nickname) {

        UserFindRes res = userService.FindUserByNickname(nickname);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/mateBridge/{mateId}")
    public ResponseEntity<ResponseDto> findUserByMateId(@PathVariable Long mateId) {

        List<MateBridge> mateBridges = mateBridgeService.FindMateBridgeByMateId(mateId);
        MateBridgeFindRes res = new MateBridgeFindRes();
        List<UserFindRes> users = new ArrayList<>();
        String creator = "";
        for (int i = 0; i < mateBridges.size(); i++) {
            if (mateBridges.get(i).getIsCreator()) {
                creator = bytesHexChanger.bytesToHex(mateBridges.get(i).getUser().getId());
            }
            users.add(userService.FindUserById(mateBridges.get(i).getUser().getId()));
        }

        res.setUsers(users);
        res.setCreator(creator);
        return new ResponseEntity<>(new ResponseDto("회원 정보 반환", res), HttpStatus.OK);
    }

    @PutMapping("/exit")
    public ResponseEntity<ResponseDto> deleteUser(@RequestBody String id) {

        return new ResponseEntity<>(new ResponseDto("회원 탈퇴 성공!", true), HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyProfile() {

        return new ResponseEntity<>(new ResponseDto("회원 정보 수정 완료", true), HttpStatus.OK);
    }

    @GetMapping("/duplicateCheck/{nickname}")
    public ResponseEntity<ResponseDto> nicknameDuplicateCheck(@PathVariable String nickname) {

        Boolean nicknameDuplicate = userService.NicknameDuplicateCheck(nickname);

        return new ResponseEntity<>(new ResponseDto("닉네임 중복 확인", nicknameDuplicate), HttpStatus.OK);
    }
}
