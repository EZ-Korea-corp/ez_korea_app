package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
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
    private final DivisionService divisionService;

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
        List<Division> divisionList = divisionService.findAllDivision();
        model.addAttribute("memberList", memberList);
        model.addAttribute("divisionList", divisionList);
        List<Member> directorList = memberService.findByRoleAndStatus(Role.ROLE_DIRECTOR, MemberStatus.FULL_TIME);
        model.addAttribute("directorList", directorList);
        List<Member> gmList = memberService.findByRoleAndStatus(Role.ROLE_GM, MemberStatus.FULL_TIME);
        model.addAttribute("gmList", gmList);
        List<Member> managerList = memberService.findByRoleAndStatus(Role.ROLE_MANAGER, MemberStatus.FULL_TIME);
        model.addAttribute("managerList", managerList);

        List<Member> teamNullMemberList = memberService.findByRoleAndStatusAndTeamIsNull(Role.ROLE_EMPLOYEE, MemberStatus.FULL_TIME);
        model.addAttribute("teamNullMemberList", teamNullMemberList);

        Member master = memberService.findByUsername("master");
        model.addAttribute("master", master);
        return "profile/chart";
    }

}
