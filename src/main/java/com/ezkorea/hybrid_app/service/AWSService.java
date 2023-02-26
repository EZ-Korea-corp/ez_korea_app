package com.ezkorea.hybrid_app.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AWSService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;

    public List<String> saveImage(List<MultipartFile> multipartFileList) {

        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFileList) {
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            try {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, originalName, multipartFile.getInputStream(), objectMetaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
            } catch (IOException e) {
                log.warn("S3Image Image Upload Fail=()", e);
            }

            String imagePath = amazonS3Client.getUrl(bucketName, originalName).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }
}
