package ssafy.journeymate.categoryservice.categoryservice.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    /**
     * 비즈니스 에러 (확인 가능한 예외 상황)
     */
    /* 400 BAD_REQUEST : 잘못된 요청 */
    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

    UN_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),

    UNAUTHORIZED_ROLE(HttpStatus.UNAUTHORIZED, "현재 유저 권한으로는 접근할 수 없는 리소스 요청입니다."),

    OUT_OF_POSSESSION(HttpStatus.UNAUTHORIZED, "해당 동작의 권한이 없습니다."),

    JWT_TOKEN_EXPIRED_EXCEPTION(HttpStatus.BAD_REQUEST, "만료된 JWT 토큰입니다."),

    INVAILD_JWT_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 Access Token입니다"),

    REFRESH_TOKEN_NOT_MATCHED(HttpStatus.BAD_REQUEST, "유효하지 않은 Refresh Token입니다."),

    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    NO_SUCH_DATA(HttpStatus.NOT_FOUND, "데이터가 존재하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."),

    JOURNEY_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),

    MATE_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),


    /**
     * 서버 에러 (서버 장애 상황)
     */
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류입니다. 잠시 후 다시 이용해주세요."),

    FILE_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 입출력 오류입니다.");


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