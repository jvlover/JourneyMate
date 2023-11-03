package com.journeymate.userservice.exception;

import com.journeymate.userservice.model.BaseException;

public class MateBridgeNotFoundException extends BaseException {

    public MateBridgeNotFoundException() {
        super(ErrorCode.MateBridge_NOT_FOUND);
    }
}

