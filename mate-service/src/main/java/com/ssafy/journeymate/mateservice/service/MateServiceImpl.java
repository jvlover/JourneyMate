package com.ssafy.journeymate.mateservice.service;


import com.ssafy.journeymate.mateservice.dto.request.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.MateDetailReq;
import com.ssafy.journeymate.mateservice.dto.request.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.entity.Mate;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import com.ssafy.journeymate.mateservice.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MateServiceImpl implements MateService{

    private final MateRepository mateRepository;


    /**
     * TODO : 브릿지 테이블은 다른 DB 에 존재 (mate bridge 테이블에 유저 아이디, 그룹 아이디, 그룹 생성자여부 저장)
     * 여행 그룹 저장
     * @param mateRegistPostReq
     * @return
     */
    @Override
    public MateRegistPostRes registMate(MateRegistPostReq mateRegistPostReq) {

        Mate mate = Mate.builder().name(mateRegistPostReq.getName())
                .destination(mateRegistPostReq.getDestination())
                .creator(mateRegistPostReq.getCreator())
                .startDate(mateRegistPostReq.getStartDate())
                .endDate(mateRegistPostReq.getEndDate())
                .build();


        Mate savedMate = mateRepository.save(mate);

        // users 는 mate bridge 에 존재 이후 관련 api 호출 필요

        return MateRegistPostRes.builder().mateId(savedMate.getId())
                .name(savedMate.getName())
                .destination(savedMate.getDestination())
                .startDate(savedMate.getStartDate())
                .endDate(savedMate.getEndDate())
                .createDate(savedMate.getCreateDate())
                .creator(savedMate.getCreator())
                .build();

//        return new ModelMapper().map(savedMate, MateRegistPostRes.class);

    }


    /**
     * TODO : 브릿지 테이블은 다른 DB 에 존재 (브릿지 테이블에 그룹원 수정되면 유저 아이디 수정 필요)
     * 그룹 수정
     * @param mateUpdatePostReq
     * @return
     */
    @Override
    public MateUpdatePostRes updateMate(MateUpdatePostReq mateUpdatePostReq) throws MateNotFoundException {

        Mate mate = mateRepository.findById(mateUpdatePostReq.getMateId()).orElseThrow(MateNotFoundException::new);

        if(!mate.getName().equals(mateUpdatePostReq.getName())){
            mate.setName(mateUpdatePostReq.getName());
        }
        if(!mate.getDestination().equals(mateUpdatePostReq.getDestination())){
            mate.setDestination(mateUpdatePostReq.getDestination());
        }
        // users 는 mate bridge 에 존재 이후 수정 추가 필요

        Mate saveMate = mateRepository.save(mate);

        return MateUpdatePostRes.builder()
                .mateId(saveMate.getId())
                .name(saveMate.getName())
                .startDate(saveMate.getStartDate())
                .endDate(saveMate.getEndDate())
                .createDate(saveMate.getCreateDate())
                .modifyDate(saveMate.getModifyDate())
                .creator(saveMate.getCreator())
                .build();

    }

    /**
     * 그룹 삭제
     *
     * @param mateDeleteReq
     * @return
     */
    @Override
    public boolean deleteMate(MateDeleteReq mateDeleteReq) throws MateNotFoundException , UnauthorizedRoleException{

        Mate mate = mateRepository.findById(mateDeleteReq.getMateId()).orElseThrow(MateNotFoundException::new);

        if(mate.getCreator().equals(mateDeleteReq.getCreator())){
            mate.deleteMate();
        }else{
            throw new UnauthorizedRoleException();
        }
        return true;
    }

    /**
     * 여행 그룹 상세 조회
     * @param mateDetailReq
     * @return
     */
    @Override
    public MateDetailRes getMateDetail(MateDetailReq mateDetailReq) {

        Mate mate = mateRepository.findById(mateDetailReq.getMateId()).orElseThrow(MateNotFoundException::new);

        return MateDetailRes.builder()
                .mateId(mate.getId())
                .name(mate.getName())
                .startDate(mate.getStartDate())
                .endDate(mate.getEndDate())
                .createDate(mate.getCreateDate())
                .creator(mate.getCreator())
                .build();
    }

}
