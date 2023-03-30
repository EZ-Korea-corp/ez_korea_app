package com.ezkorea.hybrid_app.service.notiece;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.aws.AWSService;
import com.ezkorea.hybrid_app.service.post.MemberPostReadService;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberPostReadService mprService;
    private final AWSService awsService;

    public Notice saveNewNotice(Member member, NoticeDto dto) {
        return noticeRepository.save(Notice.builder()
                .title(dto.getTitle())
                .content(dto.getContent().replaceAll("\n", "<br/>"))
                .writer(member)
                .imageList(new ArrayList<>())
                .build());
    }

    @Transactional
    public Notice updateNotice(NoticeDto dto, Member member) {
        Notice currentNotice = findNoticeById(dto.getId());
        // 기존 이미지들을 모두 삭제 후 View 단에서 다시 업로드 요청
        awsService.deleteImages(currentNotice.getImageList());
        // 이미지를 다시 등록할 수 있게 List를 비워줌
        currentNotice.setBasicInfo(dto, member);
        return noticeRepository.save(currentNotice);
    }

    public Notice findNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "번 공지사항을 찾을 수 없습니다."));
    }

    public List<Notice> findTop5NoticeOrderByUploadTime() {
        return noticeRepository.findTop5ByOrderByCreateDateDesc();
    }

    public void deleteNotice(Notice notice) {
        awsService.deleteImages(notice.getImageList());
        noticeRepository.delete(notice);
    }

    public Page<Notice> findAllNotice(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return noticeRepository.findAllByOrderByCreateDateDesc(pageable);
    }

    public Integer countAllNotice() {
        return noticeRepository.findAll().size();
    }

    public Page<Notice> findAllNotReadNotice(int page, Member member) {
        Pageable pageable = PageRequest.of(page, 10);
        List<Notice> notReadList = mprService.findNotReadListByMember(member);
        return new PageImpl<>(notReadList, pageable, notReadList.size());
    }
}
