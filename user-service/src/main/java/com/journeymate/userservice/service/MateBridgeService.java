package com.journeymate.userservice.service;

import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.entity.MateBridge;
import java.util.List;

public interface MateBridgeService {

    List<MateBridge> FindMateBridgeByMateId(Long mateId);

    List<MateBridge> registMateBridge(MateBridgeRegistPostReq mateBridgeRegistPostReq);
}
