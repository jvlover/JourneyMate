package com.journeymate.userservice.controller;

import com.journeymate.userservice.dto.ResponseDto;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.service.UserService;
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

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> socialLogin(
        @RequestBody UserRegistPostReq userRegistPostReq) {

        byte[] hexId = userService.createUUID();

        // TODO: 회원 있으면 login 없으면 registUser
        User res = userService.registUser(hexId, userRegistPostReq.getEmail(),
            userRegistPostReq.getNicnkname());

        return new ResponseEntity<>(new ResponseDto("로그인 완료!", res), HttpStatus.OK);
    }


    @GetMapping("/findByid/{id}")
    public ResponseEntity<ResponseDto> findUserById(@PathVariable String id) {

        byte[] hexId = userService.hexToBytes(id);

        UserFindRes res = userService.FindUserById(hexId);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }


    @GetMapping("/findByemail/{email}")
    public ResponseEntity<ResponseDto> findUserByEmail(@PathVariable String email) {

        UserFindRes res = userService.FindUserByEmail(email);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @GetMapping("/findBynickname/{nickname}")
    public ResponseEntity<ResponseDto> findUserByNickname(@PathVariable String nickname) {

        UserFindRes res = userService.FindUserByNickname(nickname);

        return new ResponseEntity<>(new ResponseDto("회원 정보 반환!", res), HttpStatus.OK);
    }

    @PutMapping("/exit")
    public ResponseEntity<ResponseDto> deleteUser(@RequestBody String id) {

        return new ResponseEntity<>(new ResponseDto("회원 탈퇴 성공!", true), HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyPrfile() {

        return new ResponseEntity<>(new ResponseDto("회원 정보 수정 완료", true), HttpStatus.OK);
    }

}
