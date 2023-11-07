package com.ssafy.journeymate.chatservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@NoArgsConstructor
@Document(collection = "comments")
public class Chat{

    @Id
    private String id; // MongoDB의 Document ID

    private CommentType type; // 메시지 타입
    private long mateId; // 그룹번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private Date timestamp = new Date(); // 메시지가 생성된 시간


    @Builder
    public Chat(CommentType type, long mateId, String sender, String message, long userCount) {
        this.type = type;
        this.mateId = mateId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }

    public void modifyType(CommentType type) {
        this.type = type;
    }

    public void modifyMateId(Long mateId) {
        this.mateId = mateId;
    }

    public void modifySender(String sender) {
        this.sender = sender;
    }

    public void modifyMessage(String message) {
        this.message = message;
    }

    public void modifyUserCount(Long userCount) {
        this.userCount = userCount;
    }

    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum CommentType {
        ENTER, QUIT, TALK
    }
}