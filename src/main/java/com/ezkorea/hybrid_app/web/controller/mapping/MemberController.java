package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.template.KakaoMapsApiRestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoMapsApiRestTemplate template;

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
