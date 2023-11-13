package com.ssafy.journeymate.chatservice.service;

import com.ssafy.journeymate.chatservice.entity.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ChatServiceImpl implements ChatService{

    private ChannelTopic channelTopic;
    private RedisTemplate redisTemplate;


    @Autowired
    public ChatServiceImpl(ChannelTopic channelTopic, RedisTemplate redisTemplate) {
        this.channelTopic = channelTopic;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 채팅방에 메시지 발송
     */
    @Override
    public void sendComment(Chat chat) {
        if(Chat.CommentType.TALK.equals(chat.getType())){
            chat.modifyMessage(chat.getMessage());
            chat.modifySender(chat.getSender() + "님이 메시지를 보내셨습니다.");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chat);
    }
}
