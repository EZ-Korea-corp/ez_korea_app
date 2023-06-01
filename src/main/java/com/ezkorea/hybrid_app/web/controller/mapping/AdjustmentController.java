package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.adjustment.AdjustMentService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/adj")
public class AdjustmentController {
    private final DivisionService divisionService;
    private final TeamService teamService;
    private final AdjustMentService adjustMentService;

    private final String STATUS_DIV = "division";
    private final String STATUS_TEAM = "team";

    @GetMapping("/main")
    public String showAdjustmentPage(Model model,
                                     @RequestParam(value= "adjDate", defaultValue="", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate adjDate) {
        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        log.info("payDate={}", adjDate);
        model.addAttribute("divisionDtoList",
                divisionService.findAllDivision().stream().map(Division::of).toList()
        );
        model.addAttribute("divisionList", divisionService.findAllDivision());
        return "adjustment/main";
    }

    @GetMapping("/team/{id}")
    public String showAdjustmentDetailPage(Model model, @PathVariable Long id,
                                           @RequestParam(value = "adjDate", defaultValue = "", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate adjDate) {

        Team currentTeam = teamService.findById(id);

        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        Map<String, String> saleStat = null;
        Map<String, String> memberStat = null;

        if(saleStat == null) {
            // 매출 저조자, 휴무자 저장
            memberStat = null;
            saleStat = adjustMentService.findTeamAdjustMentDefault(id);

        }

        model.addAttribute("viewName", currentTeam.getTeamName());
        model.addAttribute("currentDate", adjDate);
        model.addAttribute("defaultMap", saleStat);
        return "adjustment/detail";
    }
}
