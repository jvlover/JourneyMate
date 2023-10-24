package com.ssafy.journeymate.mateservice.service;

import com.ssafy.journeymate.mateservice.dto.request.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.MateDetailReq;
import com.ssafy.journeymate.mateservice.dto.request.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;

public interface MateService {


    public MateRegistPostRes registMate(MateRegistPostReq mateRegistPostReq);

    public MateUpdatePostRes updateMate(MateUpdatePostReq mateUpdatePostRes) throws MateNotFoundException;

    public boolean deleteMate(MateDeleteReq mateDeleteReq);

    public MateDetailRes getMateDetail(MateDetailReq mateDetailReq);

}
