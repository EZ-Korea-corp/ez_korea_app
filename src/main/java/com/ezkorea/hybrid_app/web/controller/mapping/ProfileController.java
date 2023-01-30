package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final MemberService memberService;

    @GetMapping("/{username}")
    public String showMemberProfilePage(@PathVariable String username, Model model) {
        Member currentMember = memberService.findByUsername(username);
        model.addAttribute("user", currentMember);
        return "profile/main";
    }

    @GetMapping("/setting")
    public String showMemberProfileSettingPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

        return "profile/setting";
    }

    @GetMapping("/chart/view")
    public String showMemberChart(Model model) {
        List<Member> memberList = memberService.findAllMember();
        model.addAttribute("memberList", memberList);
        return "profile/chart";
    }

}
