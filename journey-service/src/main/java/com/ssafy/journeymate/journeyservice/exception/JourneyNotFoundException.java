package com.ssafy.journeymate.journeyservice.exception;

public class JourneyNotFoundException extends BaseException {

    public JourneyNotFoundException() {
        super(ErrorCode.JOURNEY_NOT_FOUND);
    }
}
