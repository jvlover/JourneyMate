package com.journeymate.userservice.service;

import com.journeymate.userservice.dto.request.MateBridgeModifyPutReq;
import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.dto.response.MateBridgeFindRes;
import com.journeymate.userservice.dto.response.MateBridgeModifyRes;
import com.journeymate.userservice.dto.response.MateBridgeRegistRes;
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
    private final BytesHexChanger bytesHexChanger;

    @Autowired
    public MateBridgeServiceImpl(MateBridgeRepository mateBridgeRepository,
        UserRepository userRepository, BytesHexChanger bytesHexChanger) {
        this.mateBridgeRepository = mateBridgeRepository;
        this.userRepository = userRepository;
        this.bytesHexChanger = bytesHexChanger;
    }

    @Override
    public MateBridgeFindRes findMateBridgeByMateId(Long mateId) {

        List<MateBridge> mateBridges = mateBridgeRepository.findByMateId(mateId);
        MateBridgeFindRes res = new MateBridgeFindRes();
        List<User> users = new ArrayList<>();
        String creator = "";
        for (MateBridge mateBridge : mateBridges) {
            if (mateBridge.getIsCreator()) {
                creator = bytesHexChanger.bytesToHex(mateBridge.getUser().getId());
            }
            Optional<User> user = userRepository.findById(mateBridge.getUser().getId());
            if (user.isPresent()) {
                users.add(user.orElseThrow(UserNotFoundException::new));
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

            mateBridges.add(new ModelMapper().map(mateBridge, MateBridgeRegistRes.class));
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
            userMap.put(new BytesHexChanger().bytesToHex(mateBridge.getUser().getId()), 1);
        }
        for (String id : users) {
            if (userMap.containsKey(id)) {
                userMap.put(id, userMap.get(id) + 1);
            } else {
                userMap.put(id, 3);
            }
        }
        for (String id : userMap.keySet()) {
            MateBridge mateBridge = mateBridgeRepository
                .findByMateIdAndUser(mateBridgeModifyPutReq.getMateId(),
                    userRepository.findById(bytesHexChanger.hexToBytes(id))
                        .orElseThrow(UserNotFoundException::new))
                .orElseThrow(MateBridgeNotFoundException::new);
            MateBridgeModifyRes modifyRes = new ModelMapper().map(mateBridge,
                MateBridgeModifyRes.class);
            if (userMap.get(id) == 3) {
                // 새롭게 추가된 유저
                mateBridgeRepository.save(mateBridge);

                res.add(modifyRes);
            } else if (userMap.get(id) == 2) {
                // 계속 존재하는 유저
                res.add(modifyRes);
            } else if (userMap.get(id) == 1) {
                // 삭제된 유저
                mateBridge.deleteBridge();
            }
        }
        return res;
    }

}
