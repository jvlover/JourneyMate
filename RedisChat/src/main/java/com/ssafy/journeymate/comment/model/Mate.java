package com.ssafy.journeymate.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Mate implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String mateId;
    private String name;
    private long userCount; // 채팅방 인원수

    public static Mate create(String name) {
        Mate mate = new Mate();
        mate.mateId = UUID.randomUUID().toString();
        mate.name = name;
        return mate;
    }
}