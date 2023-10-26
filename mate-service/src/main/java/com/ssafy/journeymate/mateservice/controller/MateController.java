package com.ssafy.journeymate.mateservice.controller;


import com.ssafy.journeymate.mateservice.dto.request.docs.DocsRegistReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.ResponseDto;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsRegistRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import com.ssafy.journeymate.mateservice.service.MateService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<ResponseDto> updateMate(
        @ModelAttribute MateUpdatePostReq mateUpdatePostReq) throws MateNotFoundException {
        MateUpdatePostRes mateUpdatePostRes = mateService.updateMate(mateUpdatePostReq);
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
    public ResponseEntity<ResponseDto> mateInfo(@PathVariable Long mateId)
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
    public ResponseEntity<ResponseDto> registDocs(@ModelAttribute DocsRegistReq docsRegistReq,
        @RequestParam(name = "imgFile", required = false) MultipartFile imgFile)
        throws IOException {

        DocsRegistRes docsRegistRes = mateService.registDocs(docsRegistReq,
            (MultipartFile) imgFile);
        return new ResponseEntity<>(new ResponseDto("문서를 저장했습니다", docsRegistRes), HttpStatus.OK);

    }


}
