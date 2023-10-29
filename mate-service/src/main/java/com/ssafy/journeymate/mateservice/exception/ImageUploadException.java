package com.ssafy.journeymate.mateservice.exception;

public class ImageUploadException extends BaseException{

    public ImageUploadException(){
        super(ErrorCode.FILE_IO_ERROR);
    }

}
