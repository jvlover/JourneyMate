package com.ssafy.journeymate.mateservice.exception;

public class UnauthorizedRoleException extends BaseException{

    public UnauthorizedRoleException(){
        super(ErrorCode.UNAUTHORIZED_ROLE);
    }
}
