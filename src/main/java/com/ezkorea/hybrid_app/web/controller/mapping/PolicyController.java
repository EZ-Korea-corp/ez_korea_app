package com.ezkorea.hybrid_app.web.controller.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/policy")
@Slf4j
public class PolicyController {

    @GetMapping("/person")
    public String showPolicyPage() {
        return "policy/person";
    }

}
