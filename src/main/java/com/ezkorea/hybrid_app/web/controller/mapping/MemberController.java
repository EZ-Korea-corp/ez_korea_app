package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.template.KakaoMapsApiRestTemplate;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoMapsApiRestTemplate template;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "login/sign-up";
    }

    @PostMapping("/signup")
    public String doSignUp(SignUpDto dto, RedirectAttributes redirectAttributes) {
        memberService.saveNewMember(dto);
        redirectAttributes.addFlashAttribute("signUp", "회원가입이 완료되었습니다.");
        return "redirect:/login";
    }

    @GetMapping("/findPassword")
    public String showFindPasswordPage() {
        return "login/find-password";
    }

    @PostMapping("/reload")
    public String doReLogin(String username) {
        Member currentMember = memberService.findByUsername(username);
        memberService.forceAuthentication(currentMember);
        return "redirect:/";
    }

    @PostMapping("/commute")
    public String doCommute(@AuthenticationPrincipal SecurityUser securityUser,
                            @RequestParam String commuteStatus,
                            @RequestParam String location) {
        String currentLocation = template.getCurrentLocation(location);
        memberService.setCommuteTime(securityUser.getMember(), commuteStatus, currentLocation);
        return "redirect:/";
    }
}
