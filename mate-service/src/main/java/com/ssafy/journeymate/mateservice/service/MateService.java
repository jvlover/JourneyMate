package com.ssafy.journeymate.mateservice.service;

import com.ssafy.journeymate.mateservice.dto.request.docs.DocsRegistReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsRegistRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface MateService {


    public MateRegistPostRes registMate(MateRegistPostReq mateRegistPostReq);

    public MateUpdatePostRes updateMate(MateUpdatePostReq mateUpdatePostRes)
        throws MateNotFoundException;

    public boolean deleteMate(MateDeleteReq mateDeleteReq)
        throws MateNotFoundException, UnauthorizedRoleException;

    public MateDetailRes getMateDetail(Long mateId) throws MateNotFoundException;

    public DocsRegistRes registDocs(DocsRegistReq docsRegistReq, MultipartFile imgFile)
        throws IOException;


}
