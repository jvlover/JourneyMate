package com.ssafy.journeymate.comment.repository;

import com.ssafy.journeymate.comment.model.Mate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MateRepository {
    // Redis CacheKeys
    private static final String MATES = "MATES"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Mate> hashOpsMate;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회
    public List<Mate> findAllMate() {
        return hashOpsMate.values(MATES);
    }

    // 특정 채팅방 조회
    public Mate findMateById(String id) {
        return hashOpsMate.get(MATES, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public Mate createMate(String name) {
        Mate mate = Mate.create(name);
        hashOpsMate.put(MATES, mate.getMateId(), mate);
        return mate;
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String mateId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, mateId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterMateId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String mateId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + mateId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String mateId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + mateId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String mateId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + mateId)).filter(count -> count > 0).orElse(0L);
    }

}