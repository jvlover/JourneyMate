package com.ssafy.journeymate.mateservice.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;


    /**
     * 파일 변환 없이 S3 에 Multipart 파일 업로드
     *
     * @param multipartFile
     * @return
     */
    public FileUploadResult uploadFile(MultipartFile multipartFile) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();

        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = getFileName(originalFileName);

        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String file = "static/" + savedFileName;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, file,
            multipartFile.getInputStream(), objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);

        String uploadUrl = amazonS3Client.getUrl(bucket, file).toString();

        return new FileUploadResult(uploadUrl, savedFileName);
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
     *
     * @param originalFileName
     * @return
     */
    public String getFileName(String originalFileName) {
        return originalFileName + "_" + System.currentTimeMillis();
    }

    @Getter
    public static class FileUploadResult {

        private String imgUrl;
        private String fileName;

        public FileUploadResult(String imgUrl, String fileName) {
            this.imgUrl = imgUrl;
            this.fileName = fileName;
        }
    }
}

