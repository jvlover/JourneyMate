package com.ssafy.journeymate.comment.repository;


import com.ssafy.journeymate.comment.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    // 특정 채팅방의 메시지를 조회하는 메서드
    List<Comment> findByMateIdOrderByTimestampDesc(String mateId);

    // 특정 사용자가 보낸 메시지를 조회하는 메서드
    List<Comment> findBySenderOrderByTimestampAsc(String sender);

}
