package com.ssafy.journeymate.mateservice.service;


import com.ssafy.journeymate.mateservice.dto.request.docs.DocsRegistReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateDeleteReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateRegistPostReq;
import com.ssafy.journeymate.mateservice.dto.request.mate.MateUpdatePostReq;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsRegistRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.entity.Docs;
import com.ssafy.journeymate.mateservice.entity.Mate;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import com.ssafy.journeymate.mateservice.repository.DocsImgRepository;
import com.ssafy.journeymate.mateservice.repository.DocsRepository;
import com.ssafy.journeymate.mateservice.repository.MateRepository;
import com.ssafy.journeymate.mateservice.util.FileUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MateServiceImpl implements MateService {

    private final MateRepository mateRepository;

    private final DocsRepository docsRepository;

    private final DocsImgRepository docsImgRepository;

    private FileUtil fileUtil;


    /**
     * TODO : 브릿지 테이블은 다른 DB 에 존재 (mate bridge 테이블에 유저 아이디, 그룹 아이디, 그룹 생성자여부 저장)
     * 여행 그룹 저장
     *
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
     *
     * @param mateUpdatePostReq
     * @return
     */
    @Override
    public MateUpdatePostRes updateMate(MateUpdatePostReq mateUpdatePostReq)
        throws MateNotFoundException {

        Mate mate = mateRepository.findById(mateUpdatePostReq.getMateId())
            .orElseThrow(MateNotFoundException::new);

        if (!mate.getName().equals(mateUpdatePostReq.getName())) {
            mate.setName(mateUpdatePostReq.getName());
        }
        if (!mate.getDestination().equals(mateUpdatePostReq.getDestination())) {
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
    public boolean deleteMate(MateDeleteReq mateDeleteReq)
        throws MateNotFoundException, UnauthorizedRoleException {

        Mate mate = mateRepository.findById(mateDeleteReq.getMateId())
            .orElseThrow(MateNotFoundException::new);

        if (mate.getCreator().equals(mateDeleteReq.getCreator())) {
            mate.deleteMate();
        } else {
            throw new UnauthorizedRoleException();
        }
        return true;
    }

    /**
     * 여행 그룹 상세 조회
     *
     * @param mateId
     * @return
     */
    @Override
    public MateDetailRes getMateDetail(Long mateId) throws MateNotFoundException {

        Mate mate = mateRepository.findById(mateId).orElseThrow(MateNotFoundException::new);

        return MateDetailRes.builder()
            .mateId(mate.getId())
            .name(mate.getName())
            .startDate(mate.getStartDate())
            .endDate(mate.getEndDate())
            .createDate(mate.getCreateDate())
            .creator(mate.getCreator())
            .build();
    }

    /**
     * 여행 그룹 문서 저장
     *
     * @param docsRegistReq
     * @param imgFile
     * @return
     */
    @Override
    public DocsRegistRes registDocs(DocsRegistReq docsRegistReq, MultipartFile imgFile)
        throws MateNotFoundException, IOException {

        Mate mate = mateRepository.findById(docsRegistReq.getMateId())
            .orElseThrow(MateNotFoundException::new);
        Docs.DocsBuilder docs = Docs.builder()
            .mate(mate)
            .title(docsRegistReq.getTitle())
            .content(docsRegistReq.getContent())
            .userId(docsRegistReq.getUserId());

        if (imgFile.isEmpty()) {
            docs.imageExist(false).build();

            Docs savedDacs = docsRepository.save(docs);

        } else {
            docs.imageExist(true).build();

            String fileUrl =  fileUtil.uploadFile(imgFile);
            String fileName = fileUtil.getFileName(imgFile.getOriginalFilename());


        }

        return null;
    }

}
