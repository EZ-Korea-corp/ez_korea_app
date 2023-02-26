package com.ezkorea.hybrid_app.service.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.aws.S3ImageRepository;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import com.ezkorea.hybrid_app.web.dto.S3ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    private final S3ImageRepository s3ImageRepository;
    private final ManagerService managerService;

    public void findCurrentEntity(S3ImageDto dto, List<MultipartFile> multipartFileList) {
        log.info("multipartFileList.toString()={}", multipartFileList.toString());
        log.info("dto={}", dto);
        switch (dto.getEntity()) {
            case "notice" -> {
                Notice currentNotice = managerService.findNoticeById(dto.getId());
                for (String url : saveImage(dto, multipartFileList)) {
                    log.info("findCurrentEntity-url={}", url);
                    S3Image savedImage = s3ImageRepository.save(S3Image.builder()
                            .notice(currentNotice)
                            .filePath(url)
                            .fileName(url)
                            .build());
                    currentNotice.getImageList().add(savedImage);
                }
            }
        }
    }

    public List<String> saveImage(S3ImageDto dto, List<MultipartFile> multipartFileList) {

        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFileList) {
            StringBuilder sb = new StringBuilder();
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            sb.append("images").append("/").append(dto.getEntity()).append("/").append(dto.getId()).append("/").append(originalName);
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            try {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, sb.toString(), multipartFile.getInputStream(), objectMetaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );
            } catch (IOException e) {
                log.warn("S3Image Image Upload Fail=()", e);
            }

            String imagePath = amazonS3Client.getUrl(bucketName, originalName).toString(); // 접근가능한 URL 가져오기
            log.info("imagePath={}", imagePath);
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }
}
