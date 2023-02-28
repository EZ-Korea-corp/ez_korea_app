package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.service.post.MemberPostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberPostReadService mprService;


    @GetMapping("/notice")
    public String showCreateNoticePage() {

        return "notice/create";
    }

    @GetMapping("/notice/{id}")
    public String showNoticeDetailPage(@PathVariable Long id, Model model,
                                       @AuthenticationPrincipal SecurityUser securityUser) {
        Notice currentNotice = noticeService.findNoticeById(id);
        mprService.saveReadInfo(currentNotice, securityUser.getMember());
        model.addAttribute("notice", currentNotice);
        model.addAttribute("readList", mprService.findReadMemberList(currentNotice));
        model.addAttribute("notReadList", mprService.findNotReadMemberList(currentNotice));
        return "notice/detail";
    }

    @GetMapping("/notice/{id}/update")
    public String showNoticeUpdatePage(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(id));
        return "notice/update";
    }
}
