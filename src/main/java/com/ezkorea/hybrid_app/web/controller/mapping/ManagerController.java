package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('ROLE_MANAGER')")
public class ManagerController {

    private final ManagerService managerService;
    private final CommuteService commuteService;
    private final MemberService memberService;
    private final ExpensesService expensesService;

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

    @GetMapping("/division/update/{id}")
    public String showDivisionUpdatePage(Model model, @PathVariable Long id) {
        model.addAttribute("gmList", managerService.findAllMemberByRole(Role.ROLE_GM));
        model.addAttribute("division", managerService.findDivisionById(id));
        return "manager/group/manage-division-update";
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

    @GetMapping("/team/update/{id}")
    public String showUpdateTeamPage(Model model, @PathVariable Long id) {
        Team currentTeam = managerService.findTeamById(id);
        model.addAttribute("currentTeam", currentTeam);
        model.addAttribute("employeeList", managerService.findAllMemberByRoleAndStatusOrTeam(Role.ROLE_EMPLOYEE, MemberStatus.FULL_TIME, currentTeam));
        // 현재 팀의 리더인 사람 + 리더지만 팀이 없는 사람만 조회
        model.addAttribute("leaderList", managerService.findAllByRoleAndTeamIsNullOrTeam(Role.ROLE_LEADER, currentTeam));
        model.addAttribute("divisionList", managerService.findAllDivision());
        return "manager/group/manage-team-update";
    }

    @GetMapping("/member")
    public String showMemberManagingPage(Model model) {
        model.addAttribute("employeeList", managerService.findAllMemberExcludeAwait());
        return "manager/manage-member";
    }

    @GetMapping("/commute")
    public String showMemberCommutePage(@RequestParam(value="date", required=false)String date, Model model) {
        //model.addAttribute("employeeList", managerService.findAllMemberByStatus(MemberStatus.FULL_TIME));
        model.addAttribute("commuteList", commuteService.findCommuteTime(date));

        return "manager/manage-commute";
    }

    @GetMapping("/calender/{id}")
    public String showMemberCalenderPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "manager/manage-calender";
    }

    @GetMapping("/taskList")
    public String showMemberTaskPage(@RequestParam(value="id", required=false)Long id,
                                     @RequestParam(value="date", required=false)String date,
                                     Model model) {
        List<DailyTask> taskList = managerService.findTaskList(date, id);

        if(taskList.size() == 1) {
            return "redirect:/station/inOutMemberDetail?id=" + taskList.get(0).getId();
        } else if(taskList.size() > 1) {
            model.addAttribute("task", taskList.get(0));
            model.addAttribute("taskList", taskList);
            return "manager/manage-taskList";
        }

        return "redirect:/";
    }

    @GetMapping("/stat")
    public String showStatPage() {
        return "manager/manage-stat";
    }

    @GetMapping("/totalStat")
    public String showTotalStatPage() {
        return "manager/stat/manage-totalStat";
    }

    @GetMapping("/expenses")
    public String showExpensesPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page) {
        model.addAttribute("expensesList", expensesService.findAllExpenses(page));
        return "manager/expenses/list";
    }

}
