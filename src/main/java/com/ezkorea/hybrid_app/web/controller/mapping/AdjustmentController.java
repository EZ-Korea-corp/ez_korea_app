package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.app.util.Script;
import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.adjustment.AdjustmentService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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
                                     @AuthenticationPrincipal SecurityUser securityUser,
                                     RedirectAttributes redirectAttributes) {
        Member currentMember = securityUser.getMember();
        if (currentMember.getTeam() == null) {
            redirectAttributes.addFlashAttribute("warningMsg", Script.href("/", "팀 등록 후 사용해주세요."));
            return "redirect:/";
        }
        redirectAttributes.addAttribute("id", currentMember.getTeam().getId());
        return "redirect:/adj/team/{id}";
    }

    @GetMapping("/team/{id}")
    public String showAdjustmentDetailPage(Model model, @PathVariable Long id,
                                           @RequestParam(value = "adjDate", defaultValue = "", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate adjDate) {

        Team currentTeam = teamService.findById(id);

        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        AdjustmentDto dto = null;

        if (adjustMentService.existsByTeamNoAndAdjDate(id, adjDate)) {
            dto = adjustMentService.findByTeamNoAndAdjDate(id, adjDate).of();
            model.addAttribute("flag", true);
        }

        model.addAttribute("dto", dto);
        model.addAttribute("teamId", id);
        model.addAttribute("viewName", currentTeam.getTeamName());
        model.addAttribute("currentDate", adjDate);

        return "adjustment/create";
    }

}
