package com.ssafy.journeymate.mateservice.controller;


import com.ssafy.journeymate.mateservice.dto.ResponseDto;
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
import com.ssafy.journeymate.mateservice.service.MateService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/mate-service")
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    /**
     * 그룹 등록
     *
     * @param mateRegistPostReq
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> registMate(
        @ModelAttribute MateRegistPostReq mateRegistPostReq) {

        MateRegistPostRes mateRegistPostRes = mateService.registMate(mateRegistPostReq);
        return new ResponseEntity<>(new ResponseDto("회원 그룹 생성 완료", mateRegistPostRes),
            HttpStatus.OK);
    }

    /**
     * 그룹 수정
     *
     * @param mateUpdatePostReq
     * @return
     * @throws MateNotFoundException
     */
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> modifyMate(
        @ModelAttribute MateUpdatePostReq mateUpdatePostReq) throws MateNotFoundException {
        MateUpdatePostRes mateUpdatePostRes = mateService.modifyMate(mateUpdatePostReq);
        return new ResponseEntity<>(new ResponseDto("회원 그룹 수정 완료", mateUpdatePostRes),
            HttpStatus.OK);
    }


    /**
     * 그룹 삭제하기
     *
     * @param mateDeleteReq
     * @return
     * @throws UnauthorizedRoleException
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteMate(@RequestBody MateDeleteReq mateDeleteReq)
        throws MateNotFoundException, UnauthorizedRoleException {
        mateService.deleteMate(mateDeleteReq);
        return new ResponseEntity<>(new ResponseDto("회원 그룹 삭제 완료", null), HttpStatus.OK);
    }


    /**
     * 그룹 상세 정보
     *
     * @param mateId
     * @return
     */
    @GetMapping("/{mateId}")
    public ResponseEntity<ResponseDto> loadMateInfo(@PathVariable Long mateId)
        throws MateNotFoundException {

        MateDetailRes mateDetailRes = mateService.getMateDetail(mateId);
        return new ResponseEntity<>(new ResponseDto("그룹의 상세 정보입니다", mateDetailRes), HttpStatus.OK);
    }


    /**
     * 문서 저장
     *
     * @param docsRegistReq
     * @param imgFile
     * @return
     */
    @PostMapping("/docs")
    public ResponseEntity<ResponseDto> registDocs(@ModelAttribute DocsRegistPostReq docsRegistReq,
        @RequestParam(name = "imgFile", required = false) List<MultipartFile> imgFile)
        throws IOException {

        DocsRegistPostRes docsRegistRes = mateService.registDocs(docsRegistReq, imgFile);
        return new ResponseEntity<>(new ResponseDto("문서를 저장했습니다", docsRegistRes), HttpStatus.OK);

    }


    @PutMapping("/docs")
    public ResponseEntity<ResponseDto> modifyDocs(
        @ModelAttribute DocsUpdateReq docsUpdatePostReq,
        @RequestParam(name = "imgFile", required = false) List<MultipartFile> imgFile)
        throws ImageUploadException, DocsNotFoundException, UnauthorizedRoleException {

        DocsUpdateRes docsUpdateRes = mateService.modifyDocs(docsUpdatePostReq, imgFile);
        return new ResponseEntity<>(new ResponseDto("문서를 수정했습니다", docsUpdateRes), HttpStatus.OK);
    }


    /**
     * 여행 그룹 문서 삭제
     *
     * @param docsDeleteReq
     * @return
     * @throws DocsNotFoundException
     * @throws UnauthorizedRoleException
     */
    @DeleteMapping("/docs")
    public ResponseEntity<ResponseDto> deleteDocs(@RequestBody DocsDeleteReq docsDeleteReq)
        throws DocsNotFoundException, UnauthorizedRoleException {
        mateService.deleteDocs(docsDeleteReq);
        return new ResponseEntity<>(new ResponseDto("문서 삭제 완료", null), HttpStatus.OK);
    }


    /**
     * 여행 그룹 문서 상세 조회
     *
     * @param docsId
     * @return
     * @throws DocsNotFoundException
     * @throws ImageNotFoundException
     */
    @GetMapping("/docs/{docsId}")
    public ResponseEntity<ResponseDto> loadDocsDetailInfo(@PathVariable Long docsId)
        throws DocsNotFoundException, ImageNotFoundException {
        DocsDetailRes docsDetailRes = mateService.getDocsDetail(docsId);
        return new ResponseEntity<>(new ResponseDto("문서 상세 조회", docsDetailRes), HttpStatus.OK);
    }

    /**
     * 여행 그룹 전체 문서 조회
     *
     * @param mateId
     * @return
     * @throws MateNotFoundException
     * @throws ImageNotFoundException
     */
    @GetMapping("/docs/list/{mateId}")
    public ResponseEntity<ResponseDto> loadDocsListInfo(@PathVariable Long mateId)
        throws MateNotFoundException, ImageNotFoundException {
        DocsListRes docsListRes = mateService.getDocsList(mateId);
        return new ResponseEntity<>(new ResponseDto("문서 전체 조회", docsListRes), HttpStatus.OK);
    }


    /**
     * 여행 그룹 콘텐츠 저장
     *
     * @param contentRegistPostReq
     * @param imgFile
     * @return
     */
    @PostMapping("/contents")
    public ResponseEntity<ResponseDto> registContent(
        @ModelAttribute ContentRegistPostReq contentRegistPostReq,
        @RequestParam(name = "imgFile", required = true) List<MultipartFile> imgFile) {
        ContentRegistPostRes contentRegistPostRes = mateService.registContent(contentRegistPostReq,
            imgFile);
        return new ResponseEntity<>(new ResponseDto("콘텐츠를 저장했습니다.", contentRegistPostRes),
            HttpStatus.OK);
    }

    /**
     * 여행 그룹 콘텐츠 삭제
     *
     * @param contentDeleteReq
     * @return
     */
    @DeleteMapping("/contents")
    public ResponseEntity<ResponseDto> deleteContent(
        @RequestBody ContentDeleteReq contentDeleteReq) {
        mateService.deleteContent(contentDeleteReq);
        return new ResponseEntity<>(new ResponseDto("콘텐츠를 삭제했습니다.", null), HttpStatus.OK);
    }

    /**
     * 여행 그룹 콘텐츠 조회
     *
     * @param mateId
     * @return
     */
    @GetMapping("/contents/list/{mateId}")
    public ResponseEntity<ResponseDto> loadContentDetailInfo(@PathVariable Long mateId) {
        ContentListRes contentListRes = mateService.getContentDetail(mateId);
        return new ResponseEntity<>(new ResponseDto("콘텐츠를 조회했습니다", contentListRes), HttpStatus.OK);
    }


}
