package com.journeymate.userservice.service;

import com.fasterxml.uuid.Generators;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.UserFindRes;
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
    public User registUser(byte[] hexId, String email, String nickname) {

        User user = User.builder().id(hexId).email(email).nickname(nickname).build();

        User res = userRepository.save(user);

        return res;

    }

    @Override
    public Boolean logout() {
        return null;
    }


    @Override
    public Boolean NicknameDuplicateCheck(String nickname) {

        if (userRepository.findByNickname(nickname).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean UserCheck(byte[] bytesId) {

        if (userRepository.findById(bytesId).isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean Login() {
        return null;
    }

    @Override
    public UserFindRes FindUserById(byte[] bytesId) {

        User user = userRepository.findById(bytesId).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        return res;
    }

    @Override
    public UserFindRes FindUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        return res;
    }

    @Override
    public UserFindRes FindUserByNickname(String nickname) {

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        res.setId(bytesHexChanger.bytesToHex(user.getId()));

        return res;
    }

    @Override
    public void modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq) {

        User user = userRepository.findById(
                bytesHexChanger.hexToBytes(userModifyProfilePutReq.getId()))
            .orElseThrow(UserNotFoundException::new);

        user.modifyProfile(userModifyProfilePutReq.getNickname(),
            userModifyProfilePutReq.getImgUrl());
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
