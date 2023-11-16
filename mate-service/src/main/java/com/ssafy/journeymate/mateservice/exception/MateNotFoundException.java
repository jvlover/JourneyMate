package com.ssafy.journeymate.mateservice.exception;

public class MateNotFoundException extends BaseException {

    public MateNotFoundException() {
        super(ErrorCode.MATE_NOT_FOUND);
    }
}
