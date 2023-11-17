package com.journeymate.checkservice.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    /**
     * 비즈니스 에러 (확인 가능한 예외 상황)
     */
    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

    CHECKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
