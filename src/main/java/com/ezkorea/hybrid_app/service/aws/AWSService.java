package com.ezkorea.hybrid_app.service.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.aws.S3ImageRepository;
import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesRepository;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
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
    private final MemberRepository memberRepository;
    private final ExpensesRepository expensesRepository;
    private final MemberService memberService;

    /**
     * S3Image를 저장하기 위한 함수
     * @param dto entity 정보가 담긴 DTO
     * @param multipartFileList 사용자가 업로드한 파일 리스트
     * */
    @Transactional
    public void saveImageInCurrentEntity(S3ImageDto dto, List<MultipartFile> multipartFileList) {
        StringBuilder sb;
        switch (dto.getEntity()) {
            // notice : 다중 파일 업로드
            case "notice" -> {
                Notice currentNotice = noticeRepository.findById(dto.getId())
                        .orElseThrow( () -> new IdNotFoundException("해당하는 id가 존재하지 않습니다."));
                for (S3Image s3Image : makeNewS3ImageObject(dto, multipartFileList)) {
                    s3Image.setNotice(currentNotice);
                    currentNotice.getImageList().add(s3Image);
                }
            }
            // member : 단일 파일 업로드 + 기존 S3Image 객체 수정 방식
            case "member" -> {
                Member currentMember = memberRepository.findById(dto.getId())
                        .orElseThrow(() -> new IdNotFoundException("해당하는 id가 존재하지 않습니다."));

                // Member는 무조건 프로필 사진이 존재해야하기 때문에 false
                deleteS3Image(currentMember.getS3Image(), false);

                // Member의 S3Image는 삭제 후 재등록이 아닌 수정하는 방식
                for (S3Image s3Image : makeNewS3ImageObject(dto, multipartFileList)) {
                    currentMember.getS3Image().setFilePath(s3Image.getFilePath());
                    currentMember.getS3Image().setFileName(s3Image.getFileName());
                    currentMember.getS3Image().setFileRepo(s3Image.getFileRepo());
                    memberService.forceAuthentication(currentMember);
                }
            }
            // expenses : 단일 파일 업로드
            case "expenses" -> {
                Expenses currentExpenses = expensesRepository.findById(dto.getId())
                        .orElseThrow(() -> new IdNotFoundException("해당하는 id가 존재하지 않습니다."));
                S3Image newS3Image = makeNewS3ImageObject(dto, multipartFileList).get(0);
                newS3Image.setExpenses(currentExpenses);
                currentExpenses.setS3Image(newS3Image);
            }
        }
    }

    public List<S3Image> makeNewS3ImageObject(S3ImageDto dto, List<MultipartFile> multipartFileList) {
        List<S3Image> imageList = new ArrayList<>();
        StringBuilder sb;
        for (String url : saveImage(dto, multipartFileList)) {
            sb = new StringBuilder();
            sb.append("images/").append(dto.getEntity()).append("/").append(dto.getId()).append("/");
            S3Image savedImage = s3ImageRepository.save(S3Image.builder()
                    .filePath(url)
                    .fileName(url.split(sb.toString())[1])
                    .fileRepo(sb.toString())
                    .build());
            imageList.add(savedImage);
        }
        return imageList;
    }


    /**
     * 파일 이름을 새로 지정하기 위한 함수
     * @param originalFilename 업로드한 파일의 이름
     * @return 임의로 지어진 파일 이름 반환
     * */
    private String createFileName(String originalFilename) {
        // S3에서 한글 파일을 찾을 수 없기 때문에 영어로 된 임의의 이름으로 변경
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    /**
     * 파일 확장자를 가져오는 함수
     * @param fileName 확장자를 가져올 파일의 이름
     * @return 파일 확장자를 반환
     * */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 %s 입니다", fileName));
        }
    }

    /**
     * 이미지를 저장하는 함수
     * @param dto S3 bucket에 저장될 경로를 설정하기 위한 DTO
     * @param multipartFileList 업로드할 파일 리스트
     * */
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
                log.warn("S3Image Image Upload Fail={}", e.toString());
            }

            String imagePath = amazonS3Client.getUrl(bucketName, sb.toString()).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

        return imagePathList;
    }

    /**
     * 여러 이미지들을 삭제하는 함수
     * @param imageList Entity를 통해 가져온 S3Image 리스트
     * */
    public void deleteImages(List<S3Image> imageList) {
        // S3에 있는 이미지 제거
        for (S3Image s3Image : imageList) {
            deleteS3Image(s3Image, true);
        }
    }

    /**
     * 단일 이미지를 삭제하는 함수
     * @param s3Image 삭제할 S3Image 객체
     * @param isDelete DB에서 삭제할 것인지에 대한 여부
     *                 true : S3 + DB 모두 제거
     *                 false : S3 단일 제거
     * */
    public void deleteS3Image(S3Image s3Image, boolean isDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append(s3Image.getFileRepo()).append(s3Image.getFileName());

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, sb.toString());
        boolean isExistObject = amazonS3Client.doesObjectExist(bucketName, sb.toString());

        log.info("S3 : 삭제할 이미지의 Key = ({}) {}", isExistObject, deleteObjectRequest.getKey());

        if (isExistObject && deleteObjectRequest.getKey().contains("/images/static/profile-image.jpg")) {
            log.info("S3 : {} 이미지가 삭제되었습니다.", deleteObjectRequest.getKey());
            amazonS3Client.deleteObject(
                    new DeleteObjectRequest(bucketName, deleteObjectRequest.getKey())
            );
        } else {
            log.info("S3 : {} 이미지가 삭제되지 않았습니다.", deleteObjectRequest.getKey());
        }
        if (isDelete) {
            s3ImageRepository.delete(s3Image);
        }
    }
}
