package com.ezkorea.hybrid_app.service.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.aws.S3ImageRepository;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import com.ezkorea.hybrid_app.web.dto.S3ImageDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@ToString(exclude = "amazonS3Client, managerService")
public class AWSService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;
    private final S3ImageRepository s3ImageRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public void findCurrentEntity(S3ImageDto dto, List<MultipartFile> multipartFileList) {
        StringBuilder sb;
        switch (dto.getEntity()) {
            case "notice" -> {
                Notice currentNotice = noticeRepository.findById(dto.getId())
                        .orElseThrow( () -> new IdNotFoundException("해당하는 id가 존재하지 않습니다."));
                for (String url : saveImage(dto, multipartFileList)) {
                    sb = new StringBuilder();
                    sb.append("images/").append(dto.getEntity()).append("/").append(dto.getId()).append("/");
                    S3Image savedImage = s3ImageRepository.save(S3Image.builder()
                            .notice(currentNotice)
                            .filePath(url)
                            .fileName(url.split(sb.toString())[1])
                            .fileRepo(sb.toString())
                            .build());
                    currentNotice.getImageList().add(savedImage);
                }
            }
        }
    }

    private String createFileName(String originalFilename) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 %s 입니다", fileName));
        }
    }

    public List<String> saveImage(S3ImageDto dto, List<MultipartFile> multipartFileList) {

        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFileList) {
            StringBuilder sb = new StringBuilder();
            String originalName = createFileName(multipartFile.getOriginalFilename()); // 파일 이름
            sb.append("images/").append(dto.getEntity()).append("/").append(dto.getId()).append("/").append(originalName);
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

            String imagePath = amazonS3Client.getUrl(bucketName, sb.toString()).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }

    public void deleteImages(List<S3Image> imageList) {
        StringBuilder sb;
        // S3에 업로드
        for (S3Image s3Image : imageList) {
            sb = new StringBuilder();
            sb.append(s3Image.getFileRepo()).append(s3Image.getFileName());

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, sb.toString());
            boolean isExistObject = amazonS3Client.doesObjectExist(bucketName, sb.toString());

            log.info("S3 : 이미지 삭제 Object Key : {}", deleteObjectRequest.getKey());
            log.info("S3 : Object Key 존재 확인: {}", isExistObject);

            if (isExistObject) {
                amazonS3Client.deleteObject(
                        new DeleteObjectRequest(bucketName, deleteObjectRequest.getKey())
                );
            }
            s3ImageRepository.delete(s3Image);
        }
    }
}
