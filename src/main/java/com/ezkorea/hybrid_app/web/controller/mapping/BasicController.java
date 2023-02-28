package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BasicController {

    private final MemberService memberService;
    private final CommuteService commuteService;
    private final NoticeService noticeService;

    @GetMapping("/")
    public String showMainPage(Model model,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        boolean isOnTime = memberService.isOnTime(securityUser.getMember());
        model.addAttribute("noticeList", noticeService.findTop5NoticeOrderByUploadTime());
        model.addAttribute("isOnTime", isOnTime);
        if (isOnTime) {
            model.addAttribute("commute", commuteService.findCommuteTimeByMember(securityUser.getMember()));
        }
        memberService.forceAuthentication(memberService.findMemberById(securityUser.getId()));
        return "index";
    }
}
