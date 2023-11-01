package com.ssafy.journeymate.mateservice.util;


import com.ssafy.journeymate.mateservice.dto.ResponseDto;
import com.ssafy.journeymate.mateservice.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseDto> baseException(BaseException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto(e.getMessage(), null),
            e.getErrorCode().getHttpStatus());
    }
}
