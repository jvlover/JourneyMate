package com.journeymate.userservice.service;

import com.fasterxml.uuid.Generators;
import com.journeymate.userservice.dto.request.UserModifyProfilePutReq;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.exception.UserNotFoundException;
import com.journeymate.userservice.repository.UserRepository;
import java.nio.ByteBuffer;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public UserDto getUserByUserId(String userId) {
//        User user = userRepository.findByUserId(userId);
//        if(user ==null) throw new UsernameNotFoundException("User not found");
//
//        UserDto userDto = new ModelMapper().map(user,UserDto.class);
//        List<ResponseOrder> orders = new ArrayList<>();
//        userDto.setOrders(orders);
//
//        return userDto;
//    }

    @Override
    public User registUser(byte[] hexId, String email, String nickname) {

        User user = User.builder().id(hexId).email(email).nickname(nickname).build();

        System.out.println(user.toString());
        userRepository.save(user);

        return user;

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
    public Boolean UserCheck(byte[] hexId) {

        if (userRepository.findById(hexId).isPresent()) {
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
    public UserFindRes FindUserById(byte[] hexId) {

        User user = userRepository.findById(hexId).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        return res;
    }

    @Override
    public UserFindRes FindUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        return res;
    }

    @Override
    public UserFindRes FindUserByNickname(String nickname) {

        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

        UserFindRes res = new ModelMapper().map(user, UserFindRes.class);

        return res;
    }

    @Override
    public void modifyProfile(UserModifyProfilePutReq userModifyProfilePutReq) {

        User user = userRepository.findById(hexToBytes(userModifyProfilePutReq.getId()))
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
        return hexToBytes(sequentialUuidV1);
    }

    // String 을 byte[]로
    public byte[] hexToBytes(String id) {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(Long.parseUnsignedLong(id.substring(0, 16), 16));
        bb.putLong(Long.parseUnsignedLong(id.substring(16), 16));
        return bb.array();
    }

    //byte[]를 String으로
    @Override
    public String bytesToHex(byte[] hexId) {

        char[] hexChars = new char[hexId.length * 2];
        for (int i = 0; i < hexId.length; i++) {
            int v = hexId[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    @Override
    public void deleteUser(byte[] hexId) {

        User user = userRepository.findById(hexId).orElseThrow(UserNotFoundException::new);

        user.deleteUser();
    }
}
