package com.journeymate.userservice.service;

import com.fasterxml.uuid.Generators;
import com.journeymate.userservice.client.JourneyClient;
import com.journeymate.userservice.client.MateClient;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.request.UserRegistPostReq;
import com.journeymate.userservice.dto.response.DocsListFindRes.DocsListFindData;
import com.journeymate.userservice.dto.response.JourneyFindRes.JourneyFindData;
import com.journeymate.userservice.dto.response.MateFindRes.MateFindData;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.dto.response.UserRegistRes;
import com.journeymate.userservice.entity.MateBridge;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.exception.UserNotFoundException;
import com.journeymate.userservice.repository.MateBridgeRepository;
import com.journeymate.userservice.repository.UserRepository;
import com.journeymate.userservice.util.BytesHexChanger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MateBridgeRepository mateBridgeRepository;
    private final MateClient mateClient;
    private final JourneyClient journeyClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final BytesHexChanger bytesHexChanger = new BytesHexChanger();
    private final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
        MateBridgeRepository mateBridgeRepository, MateClient mateClient,
        JourneyClient journeyClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.mateBridgeRepository = mateBridgeRepository;
        this.mateClient = mateClient;
        this.journeyClient = journeyClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserRegistRes registUser(UserRegistPostReq userRegistPostReq) {

        log.info("UserService_registUser_start : " + userRegistPostReq);

        User user = userRepository.save(
            User.builder().id(createUUID()).nickname(userRegistPostReq.getNickname())
                .imgUrl(userRegistPostReq.getImgUrl()).build());

        UserRegistRes res = modelMapper.map(user, UserRegistRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        log.info("UserService_registUser_end : " + res);

        return res;
    }

//    @Override
//    public String login(String id) {
//
//        log.info("UserService_login_start : " + id);
//
//        Date now = new Date();
//
//        String accessToken = JWT.create() // JWT 토큰을 생성하는 빌더 반환
//            .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken이므로 AccessToken
//            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
//            //추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
//            //추가하실 경우 .withClaim(클래임 이름, 클래임 값) 으로 설정해주시면 됩니다
//            .withClaim(ID_CLAIM, id)
//            .sign(Algorithm.HMAC512(
//                secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
//
//        // Todo : refreshToken 발급해서 redis에 넣어놓기
//
//        log.info("UserService_login_end : " + accessToken);
//
//        return accessToken;
//    }

    @Override
    public Boolean nicknameDuplicateCheck(String nickname) {

        log.info("UserService_nicknameDuplicateCheck_start : " + nickname);

        Boolean res = userRepository.findByNickname(nickname).isPresent();

        log.info("UserService_nicknameDuplicateCheck_end : " + res);

        return res;
    }

    // 유저 있는지 체크
    @Override
    public Boolean userCheck(String id) {

        log.info("UserService_userCheck_start : " + id);

        Boolean res = userRepository.findById(bytesHexChanger.hexToBytes(id)).isPresent();

        log.info("UserService_userCheck_end : " + res);

        return res;
    }

    @Override
    public UserFindRes findUserById(String id) {

        log.info("UserService_findUserById_start : " + id);

        User user = userRepository.findById(bytesHexChanger.hexToBytes(id))
            .orElseThrow(UserNotFoundException::new);

        UserFindRes res = modelMapper.map(user, UserFindRes.class);

        res.setId(id);

        log.info("UserService_findUserById_end : " + res);

        return res;
    }

    @Override
    public UserFindRes findUserByNickname(String nickname) {

        log.info("UserService_findUserByNickname_start : " + nickname);

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

        UserFindRes res = modelMapper.map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        log.info("UserService_findUserByNickname_end : " + res);

        return res;
    }

    @Override
    public List<MateFindData> findMateById(String id) {

        log.info("UserService_findMateById_start : " + id);

        List<MateBridge> mateBridges = mateBridgeRepository.findByUserId(
            bytesHexChanger.hexToBytes(id));

        List<MateFindData> res = new ArrayList<>();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(
            "mate-find-circuitbreaker");

        for (MateBridge mateBridge : mateBridges) {

            MateFindData mateFindData = circuitBreaker.run(
                () -> mateClient.findMate(mateBridge.getMateId()).getData(),
                throwable -> new MateFindData());

            res.add(mateFindData);

        }

        log.info("UserService_findMateById_end : " + res);
        return res;
    }

    @Override
    public UserModifyRes modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq) {

        log.info("UserService_modifyProfile_start : " + userModifyProfilePutReq);

        User user = userRepository.findById(
                bytesHexChanger.hexToBytes(userModifyProfilePutReq.getId()))
            .orElseThrow(UserNotFoundException::new);

        user.modifyProfile(userModifyProfilePutReq.getNickname(),
            userModifyProfilePutReq.getImgUrl());

        UserModifyRes res = modelMapper.map(user, UserModifyRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        log.info("UserService_modifyProfile_end : " + res);

        return res;
    }

    //UUID 생성 후 byte[]로
    @Override
    public byte[] createUUID() {

        log.info("UserService_createUUID_start");

        UUID uuidV1 = Generators.timeBasedGenerator().generate();
        String[] uuidV1Parts = uuidV1.toString().split("-");
        String sequentialUUID =
            uuidV1Parts[2] + uuidV1Parts[1] + uuidV1Parts[0] + uuidV1Parts[3] + uuidV1Parts[4];

        String sequentialUuidV1 = String.join("", sequentialUUID);

        log.info("UserService_createUUID_end : " + sequentialUuidV1);

        return bytesHexChanger.hexToBytes(sequentialUuidV1);
    }

    @Override
    public void deleteUser(String id) {

        log.info("UserService_deleteUser_start : " + id);

        User user = userRepository.findById(bytesHexChanger.hexToBytes(id))
            .orElseThrow(UserNotFoundException::new);

        user.deleteUser();

        log.info("UserService_deleteUser_end : SUCCESS");

    }

    @Override
    public List<JourneyFindData> findTodayJourneyById(String id) {

        log.info("UserService_findTodayJourneyById_start : " + id);

        List<JourneyFindData> res = new ArrayList<>();

        List<MateBridge> mateBridges = mateBridgeRepository.findByUserId(
            bytesHexChanger.hexToBytes(id));

        LocalDate today = LocalDate.now();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(
            "Mate-And-Journey-find-circuitbreaker");

        for (MateBridge mateBridge : mateBridges) {

            Long mateId = mateBridge.getMateId();

            LocalDate mateDate = circuitBreaker.run(
                () -> mateClient.findMate(mateId).getData().getStartDate().toLocalDate(),
                throwable -> LocalDate.of(1970, 01, 01));

            List<JourneyFindData> journeyFindRes = circuitBreaker.run(
                () -> journeyClient.findJourneyByMateId(mateId)
                    .getData(),
                throwable -> new ArrayList<>());

            for (JourneyFindData journeyFindData : journeyFindRes) {
                if (today.isEqual(mateDate.plusDays(journeyFindData.getDay() - 1))) {

                    res.add(journeyFindData);

                }
            }
        }

        log.info("UserService_findTodayJourneyById_end : " + res);

        return res;
    }

    @Override
    public List<DocsListFindData> findDocsById(String id) {

        log.info("UserService_findDocsById_start : " + id);

        List<MateBridge> mateBridges = mateBridgeRepository.findByUserId(
            bytesHexChanger.hexToBytes(id));

        List<DocsListFindData> res = new ArrayList<>();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("docs-find-circuitbreaker");

        for (MateBridge mateBridge : mateBridges) {

            Long mateId = mateBridge.getMateId();

            List<DocsListFindData> docsListFindDatas = circuitBreaker.run(
                () -> mateClient.findDocs(mateId).getData().getDocsInfoList(),
                throwable -> new ArrayList<>());

            // 여기서 userId랑 일치하는 것들만 추가해야함
            for (DocsListFindData docsListFindData : docsListFindDatas) {

                if (docsListFindData.getUserId().equals(id)) {

                    res.add(docsListFindData);

                }
            }
        }

        log.info("UserService_findDocsById_end : " + res);

        return res;
    }

}
