package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.adjustment.LowPerformer;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.adjustment.AdjustmentService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/adj")
public class AdjustmentController {
    private final DivisionService divisionService;
    private final TeamService teamService;
    private final AdjustmentService adjustMentService;
    private final MemberService memberService;

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

        AdjustmentDto adjDto = null;
        Team currentTeam = teamService.findById(id);

        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        // 해당팀의 adjustment조회
        Adjustment adjustmentStat = adjustMentService.findAdjustmentByTeamNoAndAdjDate(id, adjDate);

        // 등록된 adjustment가 없을시 등록후 재조회
        if(adjustmentStat == null && adjDate.isEqual(LocalDate.now())) {
            adjustMentService.adjustmentMbRepository(id);
            adjustmentStat = adjustMentService.findAdjustmentByTeamNoAndAdjDate(id, adjDate);
        }

        if(adjustmentStat == null) {
            adjDto = adjustmentStat.of2();
        } else {
            adjDto = adjustmentStat.of();
        }

        model.addAttribute("adjStat", adjDto);
        model.addAttribute("viewName", currentTeam.getTeamName());
        model.addAttribute("currentDate", adjDate);
        
        return "adjustment/detail";
    }

    @GetMapping("/default/{id}")
    public String showAdjustmentDefault(Model model, @PathVariable Long id,
                                           @RequestParam(value = "adjDate", defaultValue = "", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate adjDate) {

        Team currentTeam = teamService.findById(id);

        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        // 해당팀의 adjustment조회
        adjustMentService.adjustmentMbRepository(id);
        Adjustment adjustmentStat = adjustMentService.findAdjustmentByTeamNoAndAdjDate(id, adjDate);

        model.addAttribute("adjStat", adjustmentStat.of());
        model.addAttribute("viewName", currentTeam.getTeamName());
        model.addAttribute("currentDate", adjDate);

        return "adjustment/detail";
    }
}
