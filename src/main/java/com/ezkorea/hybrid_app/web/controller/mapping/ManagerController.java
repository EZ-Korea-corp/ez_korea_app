package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('MANAGER')")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/home")
    public String showManagerPage() {
        return "manager/index";
    }

    @GetMapping("/member")
    public String showMemberManagingPage(Model model) {
        model.addAttribute("employeeList", managerService.findAllMemberByRole(Role.ROLE_EMPLOYEE));
        model.addAttribute("leaderList", managerService.findAllMemberByRole(Role.ROLE_LEADER));
        return "manager/manage-member";
    }
}
