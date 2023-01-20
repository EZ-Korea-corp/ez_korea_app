package com.ezkorea.hybrid_app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "login/sign-up";
    }
}
