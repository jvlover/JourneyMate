package com.journeymate.userservice.service;

import com.journeymate.userservice.dto.request.MateBridgeModifyPutReq;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.MateBridgeModifyRes;
import com.journeymate.userservice.dto.response.MateBridgeRegistRes;
import java.util.List;

public interface MateBridgeService {

    MateBridgeFindRes findMateBridgeByMateId(Long mateId);

    List<MateBridgeRegistRes> registMateBridge(MateBridgeRegistPostReq mateBridgeRegistPostReq);

    List<MateBridgeModifyRes> modifyMateBridge(MateBridgeModifyPutReq mateBridgeModifyPutReq);

    void deleteMateBridge(Long mateId);
}
