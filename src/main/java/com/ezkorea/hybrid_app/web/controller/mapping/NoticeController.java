package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.app.util.Script;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberPostReadService mprService;


    @GetMapping("/notice/create")
    public String showCreateNoticePage(@AuthenticationPrincipal SecurityUser securityUser) {

        if (!securityUser.getMember().getSubAuth().isPostAuth()) {
            return Script.href("/", "글을 작성할 권한이 없습니다.");
        }

        return "notice/create";
    }

    @GetMapping("/notice")
    public String showNoticeListPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        model.addAttribute("noticeList", noticeService.findAllNotice(page));

        return "notice/list";
    }

    @GetMapping("/notice/not-read")
    public String showNotReadNoticeListPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        model.addAttribute("noticeList", noticeService.findAllNotReadNotice(page, securityUser.getMember()));

        return "notice/list";
    }



    @GetMapping("/notice/{id}")
    public String showNoticeDetailPage(@PathVariable Long id, Model model,
                                       @AuthenticationPrincipal SecurityUser securityUser) {
        Notice currentNotice = noticeService.findNoticeById(id);
        mprService.saveReadInfo(currentNotice, securityUser.getMember());
        model.addAttribute("notice", currentNotice.of());
        model.addAttribute("notReadList", mprService.findNotReadMemberList(currentNotice));
        return "notice/detail";
    }

    @GetMapping("/notice/{id}/update")
    public String showNoticeUpdatePage(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findNoticeById(id));
        return "notice/update";
    }
}
