package com.ssafy.journeymate.comment.conrtroller;


import com.ssafy.journeymate.comment.model.Comment;
import com.ssafy.journeymate.comment.repository.CommentRepository;
import com.ssafy.journeymate.comment.repository.MateRepository;
import com.ssafy.journeymate.comment.service.CommentService;
import com.ssafy.journeymate.comment.service.JwtTokenProvider;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MateRepository mateRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository; // 추가

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/comment-service")
    public void comment(Comment comment, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
        comment.setSender(nickname);
        // 채팅방 인원수 세팅
        comment.setUserCount(mateRepository.getUserCount(comment.getMateId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        commentService.sendComment(comment);

        // 메시지를 MongoDB에 저장
        commentRepository.save(comment); // 추가
    }

    @GetMapping("/comment-service/{mateId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String mateId) {
        List<Comment> comments = commentRepository.findByMateIdOrderByTimestampDesc(mateId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}