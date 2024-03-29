package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stat")
@RequiredArgsConstructor
@Slf4j
public class StatController {

    @GetMapping("/main")
    public String showMemberProfilePage() {
        return "stat/main";
    }

}
