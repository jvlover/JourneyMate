package com.ssafy.journeymate.mateservice.exception;

public class ImageNotFoundException extends BaseException {

    public ImageNotFoundException() {
        super(ErrorCode.IMAGE_NOT_FOUND);
    }
}
