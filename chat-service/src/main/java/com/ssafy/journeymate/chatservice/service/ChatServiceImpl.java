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

    @Override
    public Integer getMateId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return lastIndex + 1;
        else
            return null;
    }





    /**
     * 채팅방에 메시지 발송
     */
    @Override
    public void sendComment(Chat chat) {
        if(Chat.CommentType.TALK.equals(chat.getType())){
            chat.modifyMessage(chat.getSender() + "님이 메시지를 보내셨습니다.");
            chat.modifySender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chat);
    }
}
