package com.ssafy.journeymate.chatservice.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.journeymate.chatservice.entity.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendComment(String publishComment) {
        try {
            // ChatMessage 객채로 맵핑
            Chat chat = objectMapper.readValue(publishComment, Chat.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat-service/mate/" + chat.getMateId(), chat);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}