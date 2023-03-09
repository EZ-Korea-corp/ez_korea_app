package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
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
    private final DivisionService divisionService;
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

        // 전체 사원 수
        List<Member> memberList = memberService.findAllMemberByStatus(MemberStatus.FULL_TIME);
        model.addAttribute("memberCount", memberList.size());

        // 대표 조회
        Member master = memberService.findByUsername("master");
        model.addAttribute("master", master);

        // 이사 조회
        List<Member> directorList = memberService.findByRoleAndStatus(Role.ROLE_DIRECTOR, MemberStatus.FULL_TIME);
        model.addAttribute("directorList", directorList);

        // 경리 조회
        List<Member> managerList = memberService.findByRoleAndStatus(Role.ROLE_MANAGER, MemberStatus.FULL_TIME);
        model.addAttribute("managerList", managerList);

        // 지점 조회 -> 무소속 제외
        List<Division> divisionList = divisionService.findAllDivision();
        divisionList.remove(divisionService.findDivisionByLeader(memberService.findByUsername("master")));
        model.addAttribute("divisionList", divisionList);

        // 무소속 지점 조회 (우선순위가 낮기 때문에 따로 조회)
        Division nullDivision = divisionService.findDivisionByLeader(memberService.findByUsername("master"));
        model.addAttribute("nullDivision", nullDivision);

        // 소속이 없는 일반 회원 조회
        List<Member> teamNullMemberList = memberService.findByRoleAndStatusAndTeamIsNull(Role.ROLE_EMPLOYEE, MemberStatus.FULL_TIME);
        model.addAttribute("teamNullMemberList", teamNullMemberList);


        return "profile/chart";
    }

}
