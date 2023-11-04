package com.journeymate.userservice.service;


import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.MateFindRes.MateFindData;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.dto.response.UserRegistRes;
import java.util.List;

public interface UserService {

    // 이건 소셜로그인 좀 더 공부해보자
    UserRegistRes registUser(byte[] hexId, String nickname);

    Boolean nicknameDuplicateCheck(String nickname);

    Boolean userCheck(byte[] bytesId);

    Boolean login();

    UserFindRes findUserById(byte[] bytesId);

    UserFindRes findUserByNickname(String nickname);

    List<MateFindData> findMateById(String Id);

    UserModifyRes modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq);

    byte[] createUUID();

    void deleteUser(byte[] bytesId);


}
