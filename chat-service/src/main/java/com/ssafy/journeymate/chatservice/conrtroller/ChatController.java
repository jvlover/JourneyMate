package com.ssafy.journeymate.chatservice.conrtroller;


import com.ssafy.journeymate.chatservice.dto.ResponseDto;
import com.ssafy.journeymate.chatservice.entity.Chat;
import com.ssafy.journeymate.chatservice.repository.ChatRepository;
import com.ssafy.journeymate.chatservice.service.ChatService;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;
    private final ChatRepository chatRepository; // 추가

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat-service")
    public ResponseEntity<ResponseDto> comment(Chat chat) {
        // 로그인 회원 정보로 대화명 설정
        chat.modifySender(chat.getSender());
        // 채팅방 인원수 세팅
//        comment.setUserCount(mateRepository.getUserCount(comment.getMateId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        LocalDateTime date = LocalDateTime.now();
        chat.modifyDate(date);
        chatService.sendComment(chat);
        // 메시지를 MongoDB에 저장
        chatRepository.save(chat); // 추가
        return new ResponseEntity<>(new ResponseDto("채팅 보내기 완료",null),HttpStatus.OK);
    }

    @GetMapping("/chat-service/{mateId}")
    public ResponseEntity<ResponseDto> getComments(@PathVariable long mateId) {
        List<Chat> chats = chatRepository.findByMateIdOrderByTimestampDesc(mateId);
        return new ResponseEntity<>(new ResponseDto("채팅 오름차순 정렬 완료", chats), HttpStatus.OK);
    }

}