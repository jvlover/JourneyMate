package com.journeymate.userservice.service;

import com.journeymate.userservice.dto.request.MateBridgeModifyPutReq;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.MateBridgeModifyRes;
import com.journeymate.userservice.dto.response.MateBridgeRegistRes;
import com.journeymate.userservice.dto.response.UserFindRes;
import com.journeymate.userservice.entity.MateBridge;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.exception.MateBridgeNotFoundException;
import com.journeymate.userservice.exception.UserNotFoundException;
import com.journeymate.userservice.repository.MateBridgeRepository;
import com.journeymate.userservice.repository.UserRepository;
import com.journeymate.userservice.util.BytesHexChanger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MateBridgeServiceImpl implements MateBridgeService {

    private final MateBridgeRepository mateBridgeRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final BytesHexChanger bytesHexChanger = new BytesHexChanger();

    @Autowired
    public MateBridgeServiceImpl(MateBridgeRepository mateBridgeRepository,
        UserRepository userRepository) {
        this.mateBridgeRepository = mateBridgeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MateBridgeFindRes findMateBridgeByMateId(Long mateId) {

        List<MateBridge> mateBridges = mateBridgeRepository.findByMateId(mateId);

        MateBridgeFindRes res = new MateBridgeFindRes();

        List<UserFindRes> users = new ArrayList<>();

        String creator = "";

        for (MateBridge mateBridge : mateBridges) {
            if (mateBridge.getIsCreator()) {
                creator = bytesHexChanger.bytesToHex(mateBridge.getUser().getId());
            }
            Optional<User> user = userRepository.findById(mateBridge.getUser().getId());
            if (user.isPresent()) {
                UserFindRes userFindRes = new ModelMapper().map(user.get(), UserFindRes.class);
                userFindRes.setId(bytesHexChanger.bytesToHex(user.get().getId()));
                users.add(userFindRes);
            }
        }
        res.setUsers(users);
        res.setCreator(creator);
        return res;
    }

    @Override
    public List<MateBridgeRegistRes> registMateBridge(
        MateBridgeRegistPostReq mateBridgeRegistPostReq) {

        List<String> users = mateBridgeRegistPostReq.getUsers();

        List<MateBridgeRegistRes> mateBridges = new ArrayList<>();

        for (String id : users) {
            User user = userRepository.findById(bytesHexChanger.hexToBytes(id))
                .orElseThrow(
                    UserNotFoundException::new);

            MateBridge mateBridge = MateBridge.builder().user(user)
                .mateId(mateBridgeRegistPostReq.getMateId())
                .isCreator(bytesHexChanger.bytesToHex(user.getId())
                    .equals(mateBridgeRegistPostReq.getCreator()))
                .build();

            mateBridgeRepository.save(mateBridge);

            UserFindRes userFindRes = modelMapper.map(user, UserFindRes.class);
            userFindRes.setId(bytesHexChanger.bytesToHex(user.getId()));
            MateBridgeRegistRes mateBridgeRegistRes = modelMapper.map(mateBridge,
                MateBridgeRegistRes.class);
            mateBridgeRegistRes.setUser(userFindRes);

            mateBridges.add(mateBridgeRegistRes);
        }

        return mateBridges;
    }

    @Override
    public List<MateBridgeModifyRes> modifyMateBridge(
        MateBridgeModifyPutReq mateBridgeModifyPutReq) {

        List<MateBridge> mateBridges = mateBridgeRepository.findByMateId(
            mateBridgeModifyPutReq.getMateId());

        List<String> users = mateBridgeModifyPutReq.getUsers();

        List<MateBridgeModifyRes> res = new ArrayList<>();

        HashMap<String, Integer> userMap = new HashMap<>();

        for (MateBridge mateBridge : mateBridges) {
            // 기존에 있던 user들 hashMap에 추가
            userMap.put(new BytesHexChanger().bytesToHex(mateBridge.getUser().getId()), 1);
        }
        for (String id : users) {
            if (userMap.containsKey(id)) {
                // 새롭게 추가된 유저가 기존 유저라면 +1 -> 2가 됨
                // 따라서, 만약 기존 유저가 추가되지 않았다면 (삭제됐다면) 1이 됨
                userMap.put(id, userMap.get(id) + 1);
            } else {
                // 새롭게 추가된 유저가 기존 유저가 아니라면 +3 -> 3이 됨
                userMap.put(id, 3);
            }
        }
        for (String id : userMap.keySet()) {
            if (userMap.get(id) == 3) {
                // 새롭게 추가된 유저

                MateBridge mateBridge = MateBridge.builder()
                    .user(userRepository.findById(bytesHexChanger.hexToBytes(id))
                        .orElseThrow(UserNotFoundException::new))
                    .mateId(mateBridgeModifyPutReq.getMateId()).build();

                mateBridgeRepository.save(mateBridge);

                MateBridgeModifyRes modifyRes = new ModelMapper().map(mateBridge,
                    MateBridgeModifyRes.class);

                UserFindRes userFindRes = modelMapper.map(mateBridge.getUser(), UserFindRes.class);

                userFindRes.setId(bytesHexChanger.bytesToHex(mateBridge.getUser().getId()));

                modifyRes.setUser(userFindRes);

                res.add(modifyRes);

            } else {
                MateBridge mateBridge = mateBridgeRepository
                    .findByMateIdAndUserId(mateBridgeModifyPutReq.getMateId(),
                        bytesHexChanger.hexToBytes(id))
                    .orElseThrow(MateBridgeNotFoundException::new);

                if (userMap.get(id) == 2) {
                    MateBridgeModifyRes modifyRes = new ModelMapper().map(mateBridge,
                        MateBridgeModifyRes.class);

                    User user = mateBridge.getUser();

                    UserFindRes userFindRes = modelMapper.map(user, UserFindRes.class);

                    userFindRes.setId(bytesHexChanger.bytesToHex(user.getId()));

                    modifyRes.setUser(userFindRes);
                    
                    // 계속 존재하는 유저
                    res.add(modifyRes);
                } else if (userMap.get(id) == 1) {
                    // 삭제된 유저
                    mateBridge.deleteBridge();
                }
            }
        }
        return res;
    }

}
