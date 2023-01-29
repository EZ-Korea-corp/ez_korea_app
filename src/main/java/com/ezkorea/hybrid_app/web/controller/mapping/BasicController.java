package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
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

    @GetMapping("/")
    public String showMainPage(Model model,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        boolean isOnTime = memberService.isOnTime(securityUser.getMember());
        model.addAttribute("isOnTime", isOnTime);
        if (isOnTime) {
            model.addAttribute("commute", commuteService.findCommuteTimeByMember(securityUser.getMember()));
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "login/sign-up";
    }

    @PostMapping("/signup")
    public String doSignUp(SignUpDto dto) {
        memberService.saveNewMember(dto);
        return "redirect:/login";
    }

    @PostMapping("/reload")
    public String doReLogin(String username) {
        Member currentMember = memberService.findByUsername(username);
        memberService.forceAuthentication(currentMember);
        return "redirect:/";
    }

    @PostMapping("/member")
    public String doCommute(@AuthenticationPrincipal SecurityUser securityUser,
                            @RequestParam String commuteStatus) {
        memberService.setCommuteTime(securityUser.getMember(), commuteStatus);
        return "redirect:/";
    }
}
