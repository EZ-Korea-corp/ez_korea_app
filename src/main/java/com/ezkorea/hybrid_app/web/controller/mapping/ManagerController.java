package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('ROLE_MANAGER')")
public class ManagerController {

    private final ManagerService managerService;
    private final CommuteService commuteService;

    @GetMapping("/home")
    public String showManagerPage() {
        return "manager/manage-index";
    }

    @GetMapping("/approval")
    public String showMemberApprovalPage(Model model) {
        model.addAttribute("employeeList", managerService.findAllMemberByStatus(MemberStatus.AWAIT));
        return "manager/manage-approval";
    }

    @GetMapping("/division")
    public String showDivisionPage(Model model) {
        model.addAttribute("divisionList", managerService.findAllDivision());
        return "manager/group/manage-group-main";
    }

    @GetMapping("/division/{id}")
    public String showDivisionDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("division", managerService.findDivisionById(id));
        return "manager/group/manage-division-detail";
    }

    @GetMapping("/division/create")
    public String showCreateDivisionPage(Model model) {
        model.addAttribute("gmList", managerService.findAllMemberByRoleAndDivisionIsNull(Role.ROLE_GM));
        return "manager/group/manage-division-create";
    }

    @GetMapping("/team/create")
    public String showCreateTeamPage(Model model) {
        model.addAttribute("divisionList", managerService.findAllDivision());
        model.addAttribute("leaderList", managerService.findAllMemberByRoleAndTeamIsNull(Role.ROLE_LEADER));
        model.addAttribute("employeeList", managerService.findAllMemberByRoleAndStatusAndTeamIsNull(Role.ROLE_EMPLOYEE, MemberStatus.FULL_TIME));
        return "manager/group/manage-team-create";
    }

    @GetMapping("/member")
    public String showMemberManagingPage(Model model) {
        model.addAttribute("employeeList", managerService.findAllMemberExcludeAwait());
        return "manager/manage-member";
    }

    @GetMapping("/commute")
    public String showMemberCommutePage(@RequestParam(value="date", required=false)String date, Model model) {
        model.addAttribute("employeeList", managerService.findAllMemberByStatus(MemberStatus.FULL_TIME));
        model.addAttribute("commuteList", commuteService.findCommuteTime(date));

        return "manager/manage-commute";
    }
}
