package com.journeymate.userservice.service;


import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.DocsListFindRes.DocsListFindData;
import com.journeymate.userservice.dto.response.JourneyFindRes.JourneyFindData;
import com.journeymate.userservice.dto.response.MateFindRes.MateFindData;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.dto.response.UserRegistRes;
import java.util.List;

public interface UserService {

    // 이건 소셜로그인 좀 더 공부해보자
    UserRegistRes registUser(UserRegistPostReq userRegistPostReq);

//    String login(String id);

    Boolean nicknameDuplicateCheck(String nickname);

    Boolean userCheck(String id);

    UserFindRes findUserById(String id);

    UserFindRes findUserByNickname(String nickname);

    List<MateFindData> findMateById(String id);

    UserModifyRes modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq);

    byte[] createUUID();

    void deleteUser(String id);

    List<JourneyFindData> findTodayJourneyById(String id);

    List<DocsListFindData> findDocsById(String id);
}
