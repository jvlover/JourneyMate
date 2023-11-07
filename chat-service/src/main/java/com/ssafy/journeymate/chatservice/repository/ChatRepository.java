package com.ssafy.journeymate.chatservice.repository;


import com.ssafy.journeymate.chatservice.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

    // 특정 채팅방의 메시지를 조회하는 메서드
    List<Chat> findByMateIdOrderByTimestampDesc(long mateId);

    // 특정 사용자가 보낸 메시지를 조회하는 메서드
    List<Chat> findBySenderOrderByTimestampAsc(String sender);

}
