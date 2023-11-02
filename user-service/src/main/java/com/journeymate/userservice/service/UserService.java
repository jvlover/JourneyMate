package com.journeymate.userservice.service;


import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.entity.User;

public interface UserService {

    // 이건 소셜로그인 좀 더 공부해보자
    User registUser(byte[] hexId, String nickname);

    Boolean logout();

    Boolean nicknameDuplicateCheck(String nickname);

    Boolean userCheck(byte[] bytesId);

    Boolean login();

    UserFindRes findUserById(byte[] bytesId);

    UserFindRes findUserByNickname(String nickname);

    UserModifyRes modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq);

    byte[] createUUID();

    void deleteUser(byte[] bytesId);


}
