package com.journeymate.checkservice.exception;


import com.journeymate.checkservice.model.BaseException;

public class ChecklistNotFoundException extends BaseException {

    public ChecklistNotFoundException() {
        super(ErrorCode.CHECKLIST_NOT_FOUND);
    }
}
