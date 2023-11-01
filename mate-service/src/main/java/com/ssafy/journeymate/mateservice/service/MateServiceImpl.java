package com.ssafy.journeymate.mateservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.mateservice.client.UserServiceClient;
import com.ssafy.journeymate.mateservice.dto.ResponseDto;
import com.ssafy.journeymate.mateservice.dto.request.client.MateBridgeRegistPostReq;
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
import com.ssafy.journeymate.mateservice.dto.response.content.ContentRegistPostRes.content;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsListRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsListRes.DocsInfo;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.docs.DocsUpdateRes;
import com.ssafy.journeymate.mateservice.dto.response.file.FileResposeDto;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateDetailRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateRegistPostRes;
import com.ssafy.journeymate.mateservice.dto.response.mate.MateUpdatePostRes;
import com.ssafy.journeymate.mateservice.entity.Contents;
import com.ssafy.journeymate.mateservice.entity.Docs;
import com.ssafy.journeymate.mateservice.entity.DocsImg;
import com.ssafy.journeymate.mateservice.entity.Mate;
import com.ssafy.journeymate.mateservice.exception.DocsNotFoundException;
import com.ssafy.journeymate.mateservice.exception.ImageNotFoundException;
import com.ssafy.journeymate.mateservice.exception.ImageUploadException;
import com.ssafy.journeymate.mateservice.exception.MateNotFoundException;
import com.ssafy.journeymate.mateservice.exception.UnauthorizedRoleException;
import com.ssafy.journeymate.mateservice.repository.ContentsRepository;
import com.ssafy.journeymate.mateservice.repository.DocsImgRepository;
import com.ssafy.journeymate.mateservice.repository.DocsRepository;
import com.ssafy.journeymate.mateservice.repository.MateRepository;
import com.ssafy.journeymate.mateservice.util.FileUtil;
import com.ssafy.journeymate.mateservice.util.FileUtil.FileUploadResult;
import com.ssafy.journeymate.mateservice.util.UserIdUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MateServiceImpl implements MateService {

    private final MateRepository mateRepository;

    private final DocsRepository docsRepository;

    private final DocsImgRepository docsImgRepository;

    private final ContentsRepository contentsRepository;

    private final UserServiceClient userServiceClient;


    private CircuitBreakerFactory circuitBreakerFactory;

    private final FileUtil fileUtil;


    /**
     * 여행 그룹 저장
     *
     * @param mateRegistPostReq
     * @return
     */
    @Override
    public MateRegistPostRes registMate(MateRegistPostReq mateRegistPostReq) {

        log.info("회원 등록입니다");

        Mate mate = Mate.builder().name(mateRegistPostReq.getName())
            .destination(mateRegistPostReq.getDestination())
            .creator(mateRegistPostReq.getCreator())
            .startDate(mateRegistPostReq.getStartDate())
            .endDate(mateRegistPostReq.getEndDate())
            .build();

        Mate savedMate = mateRepository.save(mate);

        MateBridgeRegistPostReq mateBridgeRegistPostReq = MateBridgeRegistPostReq.builder()
            .mateId(savedMate.getId())
            .creator(savedMate.getCreator())
            .user(mateRegistPostReq.getUsers().stream().toList())
            .build();

        log.info("user-service : user 정보 request");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-regist-circuitbreaker");
        ResponseDto responseDto = circuitBreaker.run(()-> userServiceClient.registMateBridge(mateBridgeRegistPostReq), throwable -> null);

        List<String> users = new ArrayList<>();

        if(responseDto != null){
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode data = null;
            try {
                data = objectMapper.readTree(responseDto.toString()).get("data");
            } catch (JsonProcessingException e) {
                log.error("JSON 데이터를 가져오는 과정에서 에러가 발생했습니다. ");
            }

            if (data.isArray()) {
                for (JsonNode item : data) {
                    JsonNode user = item.get("user");
                    String nickname = user.get("nickname").asText();
                    log.info("여행 그룹에 속한 회원 닉네임 입니다. ", nickname);
                    users.add(nickname);
                }
            }
        }

        return MateRegistPostRes.builder().mateId(savedMate.getId())
            .name(savedMate.getName())
            .destination(savedMate.getDestination())
            .startDate(savedMate.getStartDate())
            .endDate(savedMate.getEndDate())
            .createdDate(savedMate.getCreatedDate())
            .creator(savedMate.getCreator())
            .users(users)
            .build();
    }


    /**
     * TODO : 브릿지 테이블 수정 URL 필요
     * 그룹 수정
     *
     * @param mateUpdatePostReq
     * @return
     */
    @Override
    public MateUpdatePostRes modifyMate(MateUpdatePostReq mateUpdatePostReq)
        throws MateNotFoundException {

        log.info("mate ID : {}", mateUpdatePostReq.getMateId());

        Mate mate = mateRepository.findById(mateUpdatePostReq.getMateId())
            .orElseThrow(MateNotFoundException::new);

        if (!mate.getName().equals(mateUpdatePostReq.getName())) {
            mate.modifyName(mateUpdatePostReq.getName());
        }
        if (!mate.getDestination().equals(mateUpdatePostReq.getDestination())) {
            mate.modifyDestination(mateUpdatePostReq.getDestination());
        }
        // users 는 mate bridge 에 존재 이후 수정 추가 필요

        Mate saveMate = mateRepository.save(mate);

        return MateUpdatePostRes.builder()
            .mateId(saveMate.getId())
            .name(saveMate.getName())
            .startDate(saveMate.getStartDate())
            .endDate(saveMate.getEndDate())
            .createdDate(saveMate.getCreatedDate())
            .updatedDate(saveMate.getUpdatedDate())
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
            mate.delete();
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
    public MateDetailRes getMateDetail(Long mateId)
        throws MateNotFoundException, JsonProcessingException {

        Mate mate = mateRepository.findById(mateId).orElseThrow(MateNotFoundException::new);

        log.info("여행 그룹에 포함된 user 정보 조회");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-mate-bridge-circuitbreaker");
        ResponseDto responseDto = circuitBreaker.run(() -> userServiceClient.getMateBridgeUsers(mateId), throwable -> null);

        List<String> users = new ArrayList<>();

        if(responseDto != null){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode data = objectMapper.readTree(responseDto.getData().toString()).get("users");

            if (data.isArray()) {
                for (JsonNode item : data) {
                    String nickname = item.get("nickname").asText();
                    log.info("여행 그룹에 속한 회원 닉네임 입니다. ", nickname);
                    users.add(nickname);
                }
            }
        }

        return MateDetailRes.builder()
            .mateId(mate.getId())
            .name(mate.getName())
            .startDate(mate.getStartDate())
            .endDate(mate.getEndDate())
            .createdDate(mate.getCreatedDate())
            .creator(mate.getCreator())
            .users(users)
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
    public DocsRegistPostRes registDocs(DocsRegistPostReq docsRegistReq,
        List<MultipartFile> imgFile)
        throws MateNotFoundException, IOException {

        Mate mate = mateRepository.findById(docsRegistReq.getMateId())
            .orElseThrow(MateNotFoundException::new);

        log.info("Mate ID {}", docsRegistReq.getMateId());

        Docs docs = Docs.builder()
            .mate(mate)
            .title(docsRegistReq.getTitle())
            .content(docsRegistReq.getContent())
            .userId(docsRegistReq.getUserId())
            .imageExist(imgFile != null)
            .build();

        Docs savedDocs = docsRepository.save(docs);

        String nickname = getNickName(docsRegistReq.getUserId());

        DocsRegistPostRes.DocsRegistPostResBuilder docsRegistRes = DocsRegistPostRes.builder()
            .docsId(savedDocs.getId())
            .title(savedDocs.getTitle())
            .content(savedDocs.getContent())
            .nickname(nickname)
            .createdDate(savedDocs.getCreatedDate());

        if (imgFile != null) {

            List<FileResposeDto> imgFiles = new ArrayList<>();

            log.info("Multipart");

            for (MultipartFile multipartFile : imgFile) {
                FileUploadResult fileUploadResult = fileUtil.uploadFile(multipartFile);

                DocsImg docsImg = DocsImg.builder()
                    .docs(savedDocs)
                    .fileName(fileUploadResult.getFileName())
                    .imgUrl(fileUploadResult.getImgUrl())
                    .build();

                docsImgRepository.save(docsImg);

                FileResposeDto fileResposeDto = FileResposeDto.builder()
                    .filename(fileUploadResult.getFileName())
                    .imgUrl(fileUploadResult.getImgUrl())
                    .build();

                imgFiles.add(fileResposeDto);

            }
            docsRegistRes.imgFileInfo(imgFiles);
        }

        return docsRegistRes.build();
    }


    /**
     * 여행 그룹 문서 수정
     *
     * @param docsUpdateReq
     * @param imgFile
     * @return
     * @throws ImageUploadException
     * @throws DocsNotFoundException
     * @throws UnauthorizedRoleException
     */
    @Override
    public DocsUpdateRes modifyDocs(DocsUpdateReq docsUpdateReq, List<MultipartFile> imgFile)
        throws ImageUploadException, DocsNotFoundException, UnauthorizedRoleException {

        Docs docs = docsRepository.findById(docsUpdateReq.getDocsId()).orElseThrow(
            DocsNotFoundException::new);

        String nickname = getNickName(docsUpdateReq.getUserId());

        DocsUpdateRes.DocsUpdateResBuilder updateResBuilder = DocsUpdateRes
            .builder()
            .docsId(docs.getId())
            .nickname(nickname)
            .createdDate(docs.getCreatedDate());

        // 작성자만 수정 가능
        if (docs.getUserId().equals(docsUpdateReq.getUserId())) {

            if (!docs.getTitle().equals(docsUpdateReq.getTitle())) {
                docs.modifyTitle(docsUpdateReq.getTitle());
            }
            if (!docs.getContent().equals(docsUpdateReq.getContent())) {
                docs.modifyContent(docsUpdateReq.getContent());
            }

            List<DocsImg> docsImgs = docsImgRepository.findByDocs_id(docs.getId()).orElse(null);

            // 사진의 경우 기존 사진들 삭제 - delete 호출, s3 삭제, 새로운 사진으로 저장
            if (imgFile != null && !imgFile.isEmpty()) {

                log.info("입력 받은 이미지 파일이 존재합니다.");

                docs.modifyImageExist(true);

                // 새로 저장할 파일 이름
                List<FileUploadResult> uploadedFiles = new ArrayList<>();

                // 업로드 이미지 dto
                List<FileResposeDto> imgFiles = new ArrayList<>();

                try {
                    for (MultipartFile multipartFile : imgFile) {

                        log.info("이미지 파일 개수 {}", imgFile.size());

                        log.info("multipart original filename {}",
                            multipartFile.getOriginalFilename());

                        FileUploadResult fileUploadResult = fileUtil.uploadFile(multipartFile);

                        uploadedFiles.add(fileUploadResult);

                        log.info("S3 업로드 완료");
                    }

                } catch (IOException e) {
                    for (FileUploadResult img : uploadedFiles) {
                        e.printStackTrace();
                        fileUtil.deleteFile(img.getFileName());
                        throw new ImageUploadException();
                    }
                }

                for (FileUploadResult file : uploadedFiles) {
                    DocsImg docsImg = DocsImg.builder()
                        .docs(docs)
                        .imgUrl(file.getImgUrl())
                        .fileName(file.getFileName())
                        .build();

                    DocsImg savedDocsImg = docsImgRepository.save(docsImg);

                    FileResposeDto fileResposeDto = FileResposeDto.builder()
                        .filename(savedDocsImg.getFileName())
                        .imgUrl(savedDocsImg.getImgUrl())
                        .build();

                    imgFiles.add(fileResposeDto);

                }

                updateResBuilder.imgFileInfo(imgFiles);

            } else {
                docs.modifyImageExist(false);
                log.info("입력 받은 이미지 파일이 존재하지 않습니다.");
            }

            if (docsImgs != null) {
                for (DocsImg img : docsImgs) {
                    img.delete();
                    fileUtil.deleteFile(img.getFileName());
                }
            }

        } else {
            throw new UnauthorizedRoleException();
        }

        Docs savedDocs = docsRepository.save(docs);

        updateResBuilder.title(savedDocs.getTitle())
            .content(savedDocs.getContent())
            .updatedDate(savedDocs.getUpdatedDate());

        return updateResBuilder.build();
    }

    /**
     * 문서 삭제
     *
     * @param docsDeleteReq
     * @return
     * @throws DocsNotFoundException
     */
    @Override
    public boolean deleteDocs(DocsDeleteReq docsDeleteReq)
        throws DocsNotFoundException, UnauthorizedRoleException {

        Docs docs = docsRepository.findById(docsDeleteReq.getDocsId())
            .orElseThrow(DocsNotFoundException::new);

        if (docs.getUserId().equals(docsDeleteReq.getUserId())) {

            List<DocsImg> docsImgs = docsImgRepository.findByDocs_id(docs.getId()).orElse(null);

            if (docsImgs != null) {
                for (DocsImg img : docsImgs) {

                    img.delete();
                }
                log.info("사진 삭제 완료");
            }

            docs.delete();
        } else {
            throw new UnauthorizedRoleException();
        }
        return true;
    }


    /**
     * 여행 그룹 문서 상세 조회
     *
     * @param docsId
     * @return
     * @throws DocsNotFoundException
     */
    @Override
    public DocsDetailRes getDocsDetail(Long docsId)
        throws DocsNotFoundException, ImageNotFoundException {

        Docs docs = docsRepository.findById(docsId).orElseThrow(DocsNotFoundException::new);

        String nickname = getNickName(docs.getUserId());

        DocsDetailRes.DocsDetailResBuilder docsDetailRes = DocsDetailRes.builder()
            .title(docs.getTitle())
            .content(docs.getContent())
            .nickname(nickname)
            .createdDate(docs.getCreatedDate())
            .docsId(docs.getId());

        if (docs.getImageExist()) {

            List<DocsImg> docsImgs = docsImgRepository.findByDocs_id(docs.getId()).orElseThrow(
                ImageNotFoundException::new);

            List<FileResposeDto> imgFiles = new ArrayList<>();

            for (DocsImg img : docsImgs) {
                FileResposeDto fileResposeDto = FileResposeDto.builder()
                    .filename(img.getFileName())
                    .imgUrl(img.getImgUrl())
                    .build();

                imgFiles.add(fileResposeDto);
            }

            docsDetailRes.imgFileInfo(imgFiles);

        }
        return docsDetailRes.build();
    }

    /**
     * 여행 그룹 문서 전체 조회
     *
     * @param mateId
     * @return
     * @throws MateNotFoundException
     */
    @Override
    public DocsListRes getDocsList(Long mateId)
        throws MateNotFoundException, ImageNotFoundException {

        List<Docs> docs = docsRepository.findByMate_id(mateId)
            .orElseThrow(MateNotFoundException::new);

        List<DocsInfo> docsInfoList = new ArrayList<>();

        for (Docs doc : docs) {

            DocsListRes.DocsInfo.DocsInfoBuilder docsInfo = DocsListRes.DocsInfo.builder()
                .title(doc.getTitle())
                .docsId(doc.getId())
                .createdDate(doc.getCreatedDate());

            if (doc.getImageExist()) {

                List<FileResposeDto> imgFiles = new ArrayList<>();

                List<DocsImg> docsImgs = docsImgRepository.findByDocs_id(doc.getId())
                    .orElseThrow(ImageNotFoundException::new);

                for (DocsImg img : docsImgs) {
                    FileResposeDto fileResposeDto = FileResposeDto.builder()
                        .filename(img.getFileName())
                        .imgUrl(img.getImgUrl())
                        .build();

                    imgFiles.add(fileResposeDto);
                }

                docsInfo.imgFileInfo(imgFiles);
            }
            docsInfoList.add(docsInfo.build());
        }

        DocsListRes docsListRes = DocsListRes.builder()
            .docsInfoList(docsInfoList)
            .build();

        return docsListRes;
    }

    /**
     * 여행 그룹 콘텐츠 저장
     *
     * @param contentRegistPostReq
     * @param imgFile
     * @return
     */
    @Override
    public ContentRegistPostRes registContent(ContentRegistPostReq contentRegistPostReq,
        List<MultipartFile> imgFile) throws ImageUploadException, MateNotFoundException {

        Mate mate = mateRepository.findById(contentRegistPostReq.getMateId())
            .orElseThrow(MateNotFoundException::new);

        List<content> contentList = new ArrayList<>();

        for (MultipartFile multipartFile : imgFile) {
            try {
                FileUploadResult fileUploadResult = fileUtil.uploadFile(multipartFile);
                Contents.ContentsBuilder contents = Contents.builder()
                    .mate(mate)
                    .userId(contentRegistPostReq.getUserId())
                    .fileName(fileUploadResult.getFileName())
                    .imgUrl(fileUploadResult.getImgUrl());

                if (multipartFile.getContentType().startsWith("video/")) {
                    contents.type(true);
                } else {
                    contents.type(false);
                }

                Contents savedContent = contentsRepository.save(contents.build());

                ContentRegistPostRes.content content = ContentRegistPostRes.content.builder()
                    .contentId(savedContent.getId())
                    .createdDate(savedContent.getCreatedDate())
                    .creatorId(savedContent.getUserId())
                    .fileName(savedContent.getFileName())
                    .imgUrl(savedContent.getImgUrl())
                    .build();

                contentList.add(content);

            } catch (IOException e) {
                throw new ImageUploadException();
            }

        }

        ContentRegistPostRes contentRegistPostRes = ContentRegistPostRes.builder()
            .contentInfo(contentList).build();

        return contentRegistPostRes;

    }

    /**
     * 여행 그룹 콘텐츠 삭제
     *
     * @param contentDeleteReq
     * @return
     */
    @Override
    public void deleteContent(ContentDeleteReq contentDeleteReq) throws ImageNotFoundException {

        for (Long contentId : contentDeleteReq.getContents()) {
            Contents contents = contentsRepository.findById(contentId)
                .orElseThrow(ImageNotFoundException::new);

            contents.delete();
            fileUtil.deleteFile(contents.getFileName());
        }
    }

    /**
     * 여행 그룹 콘텐츠 조회
     *
     * @param mateId
     * @return
     */
    @Override
    public ContentListRes getContentDetail(Long mateId) {

        List<Contents> contentList = contentsRepository.findByMate_id(mateId);

        List<ContentListRes.content> contentInfo = new ArrayList<>();

        if (contentList != null) {
            for (Contents c : contentList) {

                ContentListRes.content content = ContentListRes.content.builder()
                    .contentId(c.getId())
                    .creatorId(c.getUserId())
                    .createdDate(c.getCreatedDate())
                    .fileName(c.getFileName())
                    .imgUrl(c.getImgUrl())
                    .type(c.getType())
                    .build();

                contentInfo.add(content);
            }

        }

        ContentListRes contentListRes = ContentListRes.builder()
            .contentInfo(contentInfo)
            .build();

        return contentListRes;
    }

    private String getNickName(String userId) {

        log.info("user service 호출 : 아이디로 유저 찾기");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("user-nickname-circuitbreaker");

        ResponseDto responseDto = circuitBreaker.run(() ->userServiceClient.getUserInfo(userId) ,throwable -> null);

        String nickname = " ";

        if(responseDto != null){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode data = null;
            try {
                data = objectMapper.readTree(responseDto.getData().toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("JSON 데이터를 가져오는 과정에서 에러가 발생했습니다. ");
            }
            nickname = data.get("nickname").asText();
        }

        return nickname;
    }


}
