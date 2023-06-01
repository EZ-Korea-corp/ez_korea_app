package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.adjustment.AdjustmentService;
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
    private final AdjustmentService adjustMentService;

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

        // 해당팀의 adjustment조회 (param:teamId)
        Map<String, String> adjustmentStat = null;

        // 등록된 adjustment가 없을시
        if(adjustmentStat == null) {
            // 프로시저 실행(jpa로 P_STAT_SALE_D(param:teamId))
            // 해당 프로시저로 ADJUSTMENT, DAY_OFF_MEMBER, LOW_PERFORMER 자동 할당
            // adjustmentStat 재조회후 초기화
        }

        // DAY_OFF_MEMBER 조회
        // LOW_PERFORMER 조회
        // model.addAttribute("defaultMap", saleStat);


        model.addAttribute("viewName", currentTeam.getTeamName());
        model.addAttribute("currentDate", adjDate);
        return "adjustment/detail";
    }
}
