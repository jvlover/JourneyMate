package com.ssafy.journeymate.comment.conrtroller;


import com.ssafy.journeymate.comment.model.Comment;
import com.ssafy.journeymate.comment.repository.CommentRepository;
import com.ssafy.journeymate.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/comment-service")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository; // 추가

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/")
    public void comment(Comment comment, String nickName, long mateId) {
        // 로그인 회원 정보로 대화명 설정
        comment.setSender(nickName);
        // 채팅방 인원수 세팅
//        comment.setUserCount(mateRepository.getUserCount(comment.getMateId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        commentService.sendComment(comment);

        // 메시지를 MongoDB에 저장
        commentRepository.save(comment); // 추가
    }

    @GetMapping("/{mateId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable long mateId) {
        List<Comment> comments = commentRepository.findByMateIdOrderByTimestampDesc(mateId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}