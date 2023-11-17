package com.journeymate.userservice.exception;


import com.journeymate.userservice.model.BaseException;

public class FileIOException extends BaseException {

    // 상황에 맞는 ErrorCode 의 이름을 super 안에 넣으면 됨
    // 상황에 맞는 ErrorCode 가 없다면 ErrorCode 파일에 새로 추가하여 작성
    public FileIOException() {
        super(ErrorCode.FILE_IO_ERROR);
    }

}