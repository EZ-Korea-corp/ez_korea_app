package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NoticeRestController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public ResponseEntity<Object> createNewNotice(@RequestBody NoticeDto dto,
                                                  @AuthenticationPrincipal SecurityUser securityUser) {

        Member currentMember = securityUser.getMember();

        if (!currentMember.getSubAuth().isPostAuth()) {
            return new ResponseEntity<>(Map.of("message", "글쓰기 권한이 없습니다."), HttpStatus.BAD_REQUEST);
        }

        Notice savedNotice = noticeService.saveNewNotice(currentMember, dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", savedNotice.getId()), HttpStatus.OK);
    }

    @PutMapping("/notice/{id}")
    public ResponseEntity<Object> updateNotice(@RequestBody NoticeDto dto, @PathVariable Long id,
                                               @AuthenticationPrincipal SecurityUser securityUser) {

        Notice savedNotice = noticeService.updateNotice(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", savedNotice.getId()), HttpStatus.OK);
    }
}
