package com.ezkorea.hybrid_app.service.notiece;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice saveNewNotice(Member member, NoticeDto dto) {
        return noticeRepository.save(Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build());
    }

    public Notice findNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "번 공지사항을 찾을 수 없습니다."));
    }
}
