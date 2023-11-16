package com.ssafy.journeymate.mateservice.exception;

public class DocsNotFoundException extends BaseException {

    public DocsNotFoundException() {
        super(ErrorCode.DOCS_NOT_FOUND);
    }
}
