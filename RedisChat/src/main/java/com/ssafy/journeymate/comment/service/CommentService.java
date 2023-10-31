package com.ssafy.journeymate.comment.service;

import com.ssafy.journeymate.comment.model.Comment;
import com.ssafy.journeymate.comment.repository.MateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final MateRepository mateRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public String getMateId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendComment(Comment comment) {
        comment.setUserCount(mateRepository.getUserCount(comment.getMateId()));
        if (Comment.CommentType.ENTER.equals(comment.getType())) {
            comment.setMessage(comment.getSender() + "님이 방에 입장했습니다.");
            comment.setSender("[알림]");
        } else if (Comment.CommentType.QUIT.equals(comment.getType())) {
            comment.setMessage(comment.getSender() + "님이 방에서 나갔습니다.");
            comment.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), comment);
    }

}
