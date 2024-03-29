package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.meal.MealService;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.service.post.MemberPostReadService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BasicController {

    private final MemberService memberService;
    private final CommuteService commuteService;
    private final NoticeService noticeService;
    private final MemberPostReadService mprService;
    private final MealService mealService;

    @GetMapping("/")
    public String showMainPage(Model model,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        Member currentMember = securityUser.getMember();
        boolean isOnTime = memberService.isOnTime(securityUser.getMember());
        model.addAttribute("noticeList", noticeService.findTop5NoticeOrderByUploadTime());
        model.addAttribute("noticeCount", noticeService.countAllNotice());
        model.addAttribute("noticeNotReadCount", mprService.findNotReadListByMember(currentMember).size());
        model.addAttribute("currentPage", "main");
        model.addAttribute("isOnTime", isOnTime);
        model.addAttribute("mealChk", mealService.existsMealChk(currentMember));
        if (isOnTime) {
            model.addAttribute("commute", commuteService.findCommuteTimeByMember(currentMember));
        }

        return "index";
    }
}
