package com.ezkorea.hybrid_app.service.etc;

import com.ezkorea.hybrid_app.domain.attach.Attach;
import com.ezkorea.hybrid_app.domain.attach.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachService {

    private final AttachRepository attachRepository;

    public void saveAttach(String path, List<MultipartFile> files, String id) {
        File file = new File(path);
        if(!file.exists()) file.mkdirs(); //폴더생성

        files.forEach(item -> {
            String fileName = item.getOriginalFilename();
            File changeFile = new File(path + "\\" + fileName);
            try {
                item.transferTo(changeFile); //업로드
                saveAttachPath(fileName, path, id);
                log.info("파일 업로드 성공");
            } catch (IOException e) {
                log.info("파일 업로드 실패");
                e.printStackTrace();
            }
        });


    }

    private void saveAttachPath(String fileName, String path, String id) {
        int index = path.indexOf("resources");
        path = path.substring(index - 1);

        Attach entity = Attach.builder()
                .fileId(id)
                .createDate(LocalDateTime.now())
                .fileName(fileName)
                .path(path)
                .build();
        attachRepository.save(entity);
    }

    public List<Attach> findAttachList(String id) {
        return attachRepository.findAllByFileId(id);
    }

}
