package com.example.y_kiosk_prototype.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        // 1. 파일명 중복 방지를 위해 UUID 생성
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // 2. S3에 전달할 메타데이터 설정 (파일 크기 등)
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        objMeta.setContentType(multipartFile.getContentType());

        // 3. S3로 파일 전송
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        // 4. 업로드된 파일의 공개 URL 반환
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
}
