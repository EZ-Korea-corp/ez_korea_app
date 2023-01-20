package com.ezkorea.hybrid_app.web;

import com.ezkorea.hybrid_app.service.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/")
    public String showMainPage() {
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
}
