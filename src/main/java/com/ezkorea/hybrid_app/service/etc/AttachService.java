package com.ezkorea.hybrid_app.service.etc;

import com.ezkorea.hybrid_app.domain.attach.Attach;
import com.ezkorea.hybrid_app.domain.attach.AttachRepository;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
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
                log.debug("파일 업로드 성공");
            } catch (IOException e) {
                log.error("파일 업로드 실패");
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

    public void deleteAttach(List<Long> delFiles, String path) {
        if(delFiles != null && delFiles.size() > 0) {
            delFiles.forEach(item -> {
                Attach attach = attachRepository.findById(item)
                                .orElseThrow(() -> new IdNotFoundException("파일을 찾을 수 없습니다."));
                try {
                    File file = new File(path.substring(0, path.lastIndexOf("\\"))
                                        + attach.getPath() + "/" + attach.getFileName());
                    if(file.exists()) file.delete();
                } catch (Exception e) {
                    log.error("디렉토리 파일 삭제 실패");
                }

                attachRepository.delete(attach);
            });
        }
    }
}
