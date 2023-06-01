package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.meal.Meal;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.service.meal.MealService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('ROLE_MANAGER')")
public class ManagerController {

    private final CommuteService cService;
    private final MemberService mService;
    private final TeamService tService;
    private final ExpensesService eService;
    private final DivisionService dService;
    private final SaleService saleService;
    private final MealService mealService;

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

    @GetMapping("/division/create")
    public String showCreateDivisionPage(Model model) {
        model.addAttribute("gmList", mService.findAllByRoleAndMemberStatus(Role.ROLE_GM, MemberStatus.FULL_TIME));
        return "manager/group/manage-division-create";
    }

    @GetMapping("/division/{id}")
    public String showDivisionDetailPage(Model model, @PathVariable Long id) {
        model.addAttribute("division", dService.findDivisionById(id));
        return "manager/group/manage-division-detail";
    }

    @GetMapping("/division/update/{id}")
    public String showDivisionUpdatePage(Model model, @PathVariable Long id) {
        Division currentDivision = dService.findDivisionById(id);
        model.addAttribute("gmList", mService.findAllByRoleAndMemberStatus(Role.ROLE_GM, MemberStatus.FULL_TIME));
        model.addAttribute("division", dService.findDivisionById(id));
        return "manager/group/manage-division-update";
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
        model.addAttribute("commuteList", cService.findCommuteTime(date));

        return "manager/manage-commute";
    }

    @GetMapping("/calender/{id}")
    public String showMemberCalenderPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "manager/manage-calender";
    }

    /**
     * 회원-일자별 판매목록
     * @Param id timeTable
     * @Param date 검색일자
     * */
    @GetMapping("/taskList")
    public String showMemberTablePage(@RequestParam(value="id", required=false)Long id,
                                      @RequestParam(value="date", required=false)String date,
                                      Model model) {
        TimeTable lastTable = saleService.findTableById(id);
        LocalDate searchDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        List<Map<String, Object>> resultList = new ArrayList<>();
        List<TimeTable> tableList = saleService.findTableList(searchDate, lastTable.getMember());
        tableList.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getGasStation().getId());
            map.put("date", item.getTaskDate());
            map.put("stationName", item.getGasStation().getStationName());
            map.put("stationLocation", item.getGasStation().getStationLocation());
            map.put("part", PartTime.of(item.getPart()));
            map.put("profilePath", item.getMember().getS3Image().getFilePath());

            resultList.add(map);
        });

        model.addAttribute("tableList", resultList);

        return "manager/manage-tableList";
    }

    @GetMapping("/outDetail")
    public String showSaleOutHistoryPage(@RequestParam(value="id") String id,
                                         @RequestParam(value="date") String date,
                                         Model model) {
        Map<String, String> paramMap = new HashMap<>();
        log.info("paramMap={}", paramMap);
        paramMap.put("id", id);
        paramMap.put("date", date);

        model.addAttribute("outList", saleService.findSellDetailByStationAndDate(paramMap));
        model.addAttribute("fixList", saleService.findFixDetailByStationAndDate(paramMap));

        return "manager/manager-outDetail";
    }

    @GetMapping("/stat")
    public String showStatPage() {
        return "manager/manage-stat";
    }

    @GetMapping("/totalStat")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
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

    @GetMapping("/dayStatList")
    public String showFindTotalStat(@RequestParam(value="date") String date, Model model) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("date", date);

        model.addAttribute("statList", saleService.findDayStatList(paramMap));

        return "manager/stat/manage-dayStat";
    }

    @GetMapping("/meal")
    public String showMealChkStat(@RequestParam(value="date", defaultValue="", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        model.addAttribute("memberSize", mService.findAllMemberByStatus(MemberStatus.FULL_TIME).size() - 1);
        model.addAttribute("mealList", mealService.findAllByCheckDate(date)
                .stream()
                .map(Meal::of)
                .toList());
        return "manager/manage-meal";
    }

}
