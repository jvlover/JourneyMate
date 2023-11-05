package com.journeymate.userservice.service;

import com.fasterxml.uuid.Generators;
import com.journeymate.userservice.client.JourneyClient;
import com.journeymate.userservice.client.MateClient;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
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
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MateBridgeRepository mateBridgeRepository;
    private final MateClient mateClient;
    private final JourneyClient journeyClient;

    private final BytesHexChanger bytesHexChanger = new BytesHexChanger();
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
        MateBridgeRepository mateBridgeRepository, MateClient mateClient,
        JourneyClient journeyClient) {
        this.userRepository = userRepository;
        this.mateBridgeRepository = mateBridgeRepository;
        this.mateClient = mateClient;
        this.journeyClient = journeyClient;
    }

    @Override
    public UserRegistRes registUser(byte[] hexId, String nickname) {

        log.info("UserService_registUser_start : " + hexId + " " + nickname);

        User user = userRepository.save(User.builder().id(hexId).nickname(nickname).build());

        UserRegistRes res = modelMapper.map(user, UserRegistRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        log.info("UserService_registUser_end : " + res);

        return res;
    }

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
    public Boolean login() {
        return null;
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

        for (MateBridge mateBridge : mateBridges) {

            res.add(mateClient.findMate(mateBridge.getMateId()).getData());

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

        for (MateBridge mateBridge : mateBridges) {

            Long mateId = mateBridge.getMateId();

            LocalDate mateDate = mateClient.findMate(mateId).getData().getStartDate().toLocalDate();

            List<JourneyFindData> journeyFindRes = journeyClient.findJourneyByMateId(mateId)
                .getData();

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

        for (MateBridge mateBridge : mateBridges) {

            Long mateId = mateBridge.getMateId();

            List<DocsListFindData> docsListFindData = mateClient.findDocs(mateId).getData();

            res.addAll(docsListFindData);
        }

        log.info("UserService_findDocsById_end : " + res);

        return res;
    }

}
