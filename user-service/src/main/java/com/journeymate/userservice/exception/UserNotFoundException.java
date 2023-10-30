package com.journeymate.userservice.exception;


import com.journeymate.userservice.model.BaseException;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
