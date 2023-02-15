package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DivisionController {

    private final MemberService memberService;

    @GetMapping("/team")
    public String saveTeam(Model model) {
        model.addAttribute("members", memberService.findByRole(Role.ROLE_EMPLOYEE));
        return "team/member-view";
    }
}
