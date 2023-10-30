package com.journeymate.userservice.service;


import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.entity.User;

public interface UserService {

    // 이건 소셜로그인 좀 더 공부해보자
    User registUser(byte[] hexId, String email, String nickname);

    Boolean logout();

    Boolean NicknameDuplicateCheck(String nickname);

    Boolean UserCheck(byte[] bytesId);

    Boolean Login();

    UserFindRes FindUserById(byte[] bytesId);

    UserFindRes FindUserByEmail(String email);

    UserFindRes FindUserByNickname(String nickname);

    void modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq);

    byte[] createUUID();

    void deleteUser(byte[] bytesId);


}
