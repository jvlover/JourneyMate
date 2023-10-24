package com.journeymate.userservice.service;

import com.journeymate.userservice.dto.request.MateBridgeRegistPostReq;
import com.journeymate.userservice.entity.MateBridge;
import com.journeymate.userservice.entity.User;
import com.journeymate.userservice.exception.UserNotFoundException;
import com.journeymate.userservice.repository.MateBridgeRepository;
import com.journeymate.userservice.repository.UserRepository;
import com.journeymate.userservice.util.BytesHexChanger;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
    public List<MateBridge> FindMateBridgeByMateId(Long mateId) {

        List<MateBridge> mateBridges = mateBridgeRepository.findByMateId(mateId);

        return mateBridges;
    }

    @Override
    public List<MateBridge> registMateBridge(MateBridgeRegistPostReq mateBridgeRegistPostReq) {

        List<String> users = mateBridgeRegistPostReq.getUsers();
        List<MateBridge> mateBridges = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            User user = userRepository.findById(bytesHexChanger.hexToBytes(users.get(i)))
                .orElseThrow(
                    UserNotFoundException::new);

            MateBridge mateBridge = MateBridge.builder().user(user)
                .mateId(mateBridgeRegistPostReq.getMateId())
                .isCreator(bytesHexChanger.bytesToHex(user.getId())
                    .equals(mateBridgeRegistPostReq.getCreator()))
                .build();

            mateBridges.add(mateBridgeRepository.save(mateBridge));
        }

        return mateBridges;
    }
}
