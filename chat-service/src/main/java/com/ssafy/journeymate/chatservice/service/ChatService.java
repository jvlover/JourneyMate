package com.ssafy.journeymate.chatservice.service;

import com.ssafy.journeymate.chatservice.entity.Chat;


public interface ChatService {

    Integer getMateId(String destination);

    void sendComment(Chat chat);

}
