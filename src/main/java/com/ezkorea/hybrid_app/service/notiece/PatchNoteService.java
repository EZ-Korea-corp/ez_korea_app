package com.ezkorea.hybrid_app.service.notiece;

import com.ezkorea.hybrid_app.domain.note.PatchNote;
import com.ezkorea.hybrid_app.domain.note.PatchNoteRepository;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatchNoteService {

    private final PatchNoteRepository pnRepository;

    @Transactional(readOnly = true)
    public Page<PatchNote> findAllNote(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return pnRepository.findAllByOrderByCreateDateDesc(pageable);
    }

    public PatchNote saveNewNote(NoticeDto dto) {
        return pnRepository.save(PatchNote.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build());
    }

    @Transactional(readOnly = true)
    public PatchNote findById(Long id) {
        return pnRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "번 노트는 존재하지 않습니다."));
    }

    public void deleteNote(Long id) {
        pnRepository.delete(findById(id));
    }

    @Transactional
    public PatchNote updateNote(Long id, NoticeDto dto) {
        PatchNote currentNote = findById(id);
        currentNote.setTitle(dto.getTitle());
        currentNote.setContent(dto.getContent());
        return currentNote;
    }
}
