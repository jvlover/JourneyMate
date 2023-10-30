package com.journeymate.userservice.exception;

import com.journeymate.userservice.model.BaseException;

/**
 * 문제 등록 시 이미지 업로드 실패 Exception
 */
public class ImageUploadFailException extends BaseException {

    public ImageUploadFailException() {
        super(ErrorCode.IMAGE_UPLOAD_FAIL);
    }
}
