package com.ssafy.journeymate.comment.service;

import com.ssafy.journeymate.comment.model.Comment;
import com.ssafy.journeymate.comment.model.Comment.CommentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;


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
    public void sendComment(Comment comment) {
        if(Comment.CommentType.TALK.equals(comment.getType())){
            comment.setMessage(comment.getSender() + "님이 메시지를 보내셨습니다.");
            comment.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), comment);
    }

}
