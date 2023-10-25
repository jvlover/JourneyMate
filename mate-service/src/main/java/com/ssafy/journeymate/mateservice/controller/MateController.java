package com.ssafy.journeymate.mateservice.controller;


import com.ssafy.journeymate.mateservice.dto.request.mate.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import com.ssafy.journeymate.mateservice.service.MateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mate-service")
@RequiredArgsConstructor
public class MateController {

    private final MateService mateService;

    /**
     * 그룹 등록
     * @param mateRegistPostReq
     * @return
     */
    @PostMapping("/regist")
    public ResponseEntity<MateRegistPostRes> registMate(@ModelAttribute MateRegistPostReq mateRegistPostReq){
        return ResponseEntity.ok(mateService.registMate(mateRegistPostReq));
    }

    /**
     * 그룹 수정
     * @param mateUpdatePostReq
     * @return
     * @throws MateNotFoundException
     */
    @PutMapping("/update")
    public ResponseEntity<MateUpdatePostRes> updateMate(@ModelAttribute MateUpdatePostReq mateUpdatePostReq) throws MateNotFoundException {
        return ResponseEntity.ok(mateService.updateMate(mateUpdatePostReq));
    }


    /**
     * 그룹 삭제하기
     * @param mateDeleteReq
     * @return
     * @throws UnauthorizedRoleException
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMate(@RequestBody MateDeleteReq mateDeleteReq) throws UnauthorizedRoleException {
            mateService.deleteMate(mateDeleteReq);
            return ResponseEntity.ok(null);
    }


    /**
     * 그룹 상세 정보
     * @param mateId
     * @return
     */
    @GetMapping("/{mateId}")
    public ResponseEntity<MateDetailRes> mateInfo(@PathVariable Long mateId){
        return ResponseEntity.ok(mateService.getMateDetail(mateId));
    }








}
