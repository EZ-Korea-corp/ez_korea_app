package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('ROLE_MANAGER')")
public class ManagerController {

    private final ManagerService managerService;
    private final CommuteService cService;
    private final MemberService mService;
    private final TeamService tService;
    private final ExpensesService eService;
    private final DivisionService dService;

    @GetMapping("/home")
    public String showManagerPage() {
        return "manager/manage-index";
    }

    @GetMapping("/approval")
    public String showMemberApprovalPage(Model model) {
        model.addAttribute("employeeList", mService.findAllMemberByStatus(MemberStatus.AWAIT));
        return "manager/manage-approval";
    }

    @GetMapping("/division")
    public String showDivisionPage(Model model) {
        model.addAttribute("divisionList", dService.findAllDivision());
        return "manager/group/manage-group-main";
    }

    @GetMapping("/division/{id}")
    public String showDivisionDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("division", dService.findDivisionById(id));
        return "manager/group/manage-division-detail";
    }

    @GetMapping("/division/update/{id}")
    public String showDivisionUpdatePage(Model model, @PathVariable Long id) {
        Division currentDivision = dService.findDivisionById(id);
        model.addAttribute("gmList", mService.findByRoleAndDivisionAndDivisionNull(Role.ROLE_GM, MemberStatus.FULL_TIME, currentDivision));
        model.addAttribute("division", dService.findDivisionById(id));
        return "manager/group/manage-division-update";
    }

    @GetMapping("/division/create")
    public String showCreateDivisionPage(Model model) {
        model.addAttribute("gmList", mService.findByRoleAndDivisionIsNull(Role.ROLE_GM, MemberStatus.FULL_TIME));
        return "manager/group/manage-division-create";
    }

    @GetMapping("/team/create")
    public String showCreateTeamPage(Model model) {
        model.addAttribute("divisionList", dService.findAllDivision());
        model.addAttribute("leaderList", mService.findByRoleAndStatusAndTeamIsNull(Role.ROLE_LEADER, MemberStatus.FULL_TIME));
        model.addAttribute("employeeList", mService.findByRoleAndStatusAndTeamIsNull(Role.ROLE_EMPLOYEE, MemberStatus.FULL_TIME));
        return "manager/group/manage-team-create";
    }

    @GetMapping("/team/update/{id}")
    public String showUpdateTeamPage(Model model, @PathVariable Long id) {
        Team currentTeam = tService.findById(id);
        model.addAttribute("currentTeam", currentTeam);
        model.addAttribute("employeeList", mService.findAllByRoleAndTeamIsNullOrTeam(Role.ROLE_EMPLOYEE, currentTeam, MemberStatus.FULL_TIME));
        // 현재 팀의 리더인 사람 + 리더지만 팀이 없는 사람만 조회
        model.addAttribute("leaderList", mService.findAllByRoleAndTeamIsNullOrTeam(Role.ROLE_LEADER, currentTeam, MemberStatus.FULL_TIME));
        model.addAttribute("divisionList", dService.findAllDivision());
        return "manager/group/manage-team-update";
    }

    @GetMapping("/member")
    public String showMemberManagingPage(Model model) {
        model.addAttribute("employeeList", mService.findAllMemberExcludeStatus(MemberStatus.AWAIT));
        return "manager/manage-member";
    }

    @GetMapping("/commute")
    public String showMemberCommutePage(@RequestParam(value="date", required=false)String date, Model model) {
        //model.addAttribute("employeeList", managerService.findAllMemberByStatus(MemberStatus.FULL_TIME));
        model.addAttribute("commuteList", cService.findCommuteTime(date));

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

    @GetMapping("/expenses/{status}")
    public String showExpensesPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page,
                                   @RequestParam(value="payDate", defaultValue="", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate payDate,
                                   @PathVariable String status) {

        ExpensesStatus currentStatus = ExpensesStatus.valueOf(status);

        if (payDate == null) {
            model.addAttribute("expensesList", eService.findAllExpensesByStatus(page, currentStatus));
        } else {
            model.addAttribute("expensesList", eService.findAllExpensesByPayDate(page, payDate, currentStatus));
        }
        return "manager/expenses/list";
    }

}
