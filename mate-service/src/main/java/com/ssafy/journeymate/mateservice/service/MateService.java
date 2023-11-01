package com.ssafy.journeymate.mateservice.service;

import com.ssafy.journeymate.mateservice.dto.request.content.ContentDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.content.ContentRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.docs.DocsDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.docs.DocsRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.docs.DocsUpdateReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.content.ContentListRes;
import com.ssafy.journeymate.mateservice.dto.response.content.ContentRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsListRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsUpdateRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.exception.DocsNotFoundException;
import com.ssafy.journeymate.mateservice.exception.ImageNotFoundException;
import com.ssafy.journeymate.mateservice.exception.ImageUploadException;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MateService {


    public MateRegistPostRes registMate(MateRegistPostReq mateRegistPostReq);

    public MateUpdatePostRes modifyMate(MateUpdatePostReq mateUpdatePostRes)
        throws MateNotFoundException;

    public boolean deleteMate(MateDeleteReq mateDeleteReq)
        throws MateNotFoundException, UnauthorizedRoleException;

    public MateDetailRes getMateDetail(Long mateId) throws MateNotFoundException;

    public DocsRegistPostRes registDocs(DocsRegistPostReq docsRegistReq,
        List<MultipartFile> imgFile)
        throws MateNotFoundException, IOException;

    public DocsUpdateRes modifyDocs(DocsUpdateReq docsUpdateReq, List<MultipartFile> imgFile)
        throws ImageUploadException, DocsNotFoundException, UnauthorizedRoleException;


    public boolean deleteDocs(DocsDeleteReq docsDeleteReq)
        throws DocsNotFoundException, UnauthorizedRoleException;

    public DocsDetailRes getDocsDetail(Long docsId)
        throws DocsNotFoundException, ImageNotFoundException;

    public DocsListRes getDocsList(Long mateId)
        throws MateNotFoundException, ImageNotFoundException;

    public ContentRegistPostRes registContent(ContentRegistPostReq contentRegistPostReq,
        List<MultipartFile> imgFile) throws ImageUploadException, MateNotFoundException;

    public void deleteContent(ContentDeleteReq contentDeleteReq) throws ImageNotFoundException;

    public ContentListRes getContentDetail(Long mateId);


}
