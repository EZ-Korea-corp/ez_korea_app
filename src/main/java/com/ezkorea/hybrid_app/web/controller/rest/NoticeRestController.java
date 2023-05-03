package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NoticeRestController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public ResponseData.ApiResult<?> createNewNotice(@RequestBody NoticeDto dto,
                                                     @AuthenticationPrincipal SecurityUser securityUser) {

        Member currentMember = securityUser.getMember();

        if (!currentMember.getSubAuth().isPostAuth()) {
            return ResponseData.error("글쓰기 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        NoticeDto mappedDto = noticeService.saveNewNotice(currentMember, dto).of();

        return ResponseData.success(mappedDto, "저장되었습니다.");
    }

    @PutMapping("/notice/{id}")
    public ResponseData.ApiResult<?> updateNotice(@RequestBody NoticeDto dto, @PathVariable Long id,
                                               @AuthenticationPrincipal SecurityUser securityUser) {

        NoticeDto mappedDto = noticeService.updateNotice(dto, securityUser.getMember()).of();

        return ResponseData.success(mappedDto, "수정되었습니다.");
    }

    @DeleteMapping("/notice/{id}")
    public ResponseData.ApiResult<?> DeleteNotice(@PathVariable Long id,
                                               @AuthenticationPrincipal SecurityUser securityUser) {

        Notice currentNotice = noticeService.findNoticeById(id);
        if (securityUser.getMember().getSubAuth().isPostAuth()) {
            noticeService.deleteNotice(currentNotice);
        }

        return ResponseData.success(null, "삭제되었습니다.");
    }

}