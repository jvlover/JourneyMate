package com.ssafy.journeymate.mateservice.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;


    /**
     * 파일 변환 없이 S3 에 Multipart 파일 업로드
     *
     * @param multipartFile
     * @return
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();

        String originalFileName = multipartFile.getOriginalFilename();

        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String file = "static/" + getFileName(originalFileName);

        amazonS3Client.putObject(bucket, file, multipartFile.getInputStream(), objectMetadata);

        return amazonS3Client.getUrl(bucket, file).toString();
    }

    /**
     * 파일 삭제
     *
     * @param fileName
     */
    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
    }

    /**
     * 파일 이름 가져오기
     * @param originalFileName
     * @return
     */
    public String getFileName(String originalFileName){
        return UUID.randomUUID() + originalFileName;
    }
}
