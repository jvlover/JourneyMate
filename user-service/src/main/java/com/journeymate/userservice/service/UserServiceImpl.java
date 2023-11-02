package com.journeymate.userservice.service;

import com.fasterxml.uuid.Generators;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.dto.response.UserModifyRes;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.exception.UserNotFoundException;
import com.journeymate.userservice.repository.UserRepository;
import com.journeymate.userservice.util.BytesHexChanger;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BytesHexChanger bytesHexChanger;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BytesHexChanger bytesHexChanger) {
        this.userRepository = userRepository;
        this.bytesHexChanger = bytesHexChanger;
    }

    @Override
    public User registUser(byte[] hexId, String nickname) {

        User user = User.builder().id(hexId).nickname(nickname).build();

        return userRepository.save(user);

    }

    @Override
    public Boolean logout() {
        return null;
    }


    @Override
    public Boolean nicknameDuplicateCheck(String nickname) {

        return userRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public Boolean userCheck(byte[] bytesId) {

        return userRepository.findById(bytesId).isPresent();
    }

    @Override
    public Boolean login() {
        return null;
    }

    @Override
    public UserFindRes findUserById(byte[] bytesId) {

        User user = userRepository.findById(bytesId).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        return res;
    }

    @Override
    public UserFindRes findUserByNickname(String nickname) {

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        return res;
    }

    @Override
    public UserModifyRes modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq) {

        User user = userRepository.findById(
                bytesHexChanger.hexToBytes(userModifyProfilePutReq.getId()))
            .orElseThrow(UserNotFoundException::new);

        user.modifyProfile(userModifyProfilePutReq.getNickname(),
            userModifyProfilePutReq.getImgUrl());

        return new ModelMapper().map(user, UserModifyRes.class);

    }

    //UUID 생성 후 byte[]로
    @Override
    public byte[] createUUID() {

        UUID uuidV1 = Generators.timeBasedGenerator().generate();
        String[] uuidV1Parts = uuidV1.toString().split("-");
        String sequentialUUID =
            uuidV1Parts[2] + uuidV1Parts[1] + uuidV1Parts[0] + uuidV1Parts[3] + uuidV1Parts[4];

        String sequentialUuidV1 = String.join("", sequentialUUID);
        return bytesHexChanger.hexToBytes(sequentialUuidV1);
    }

    @Override
    public void deleteUser(byte[] bytesId) {

        User user = userRepository.findById(bytesId).orElseThrow(UserNotFoundException::new);

        user.deleteUser();
    }
}
