package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final MemberService memberService;
    private final CommuteService commuteService;
    private final ModelMapper mapper;

    @GetMapping("/profile/{username}")
    public String showMemberProfilePage(@PathVariable String username, Model model) {
        Member currentMember = memberService.findByUsername(username);
        String divisionName = memberService.makeDivisionName(currentMember);
        model.addAttribute("user", currentMember);
        model.addAttribute("divisionName", divisionName);
        return "profile/main";
    }

    @GetMapping("/settings/profile")
    public String showMemberProfileSettingPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

        return "profile/setting-profile";
    }

    @GetMapping("/settings/account")
    public String showMemberAccountSettingPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        Member currentMember = memberService.findByUsername(securityUser.getUsername());
        ProfileDto dto = mapper.map(currentMember, ProfileDto.class);
        model.addAttribute("memberVo", dto);
        return "profile/setting-account";
    }

    @GetMapping("/settings/password")
    public String showMemberPasswordSettingPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

        return "profile/setting-password";
    }

    @GetMapping("/profile/chart/view")
    public String showMemberChart(Model model) {
        // 조직도 조회
        model.addAttribute("profileList", commuteService.findMemberChart());
        return "profile/chart";
    }

}
