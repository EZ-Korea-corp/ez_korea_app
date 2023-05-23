package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
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

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/adj")
public class AdjustmentController {
    private final DivisionService divisionService;
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

    @GetMapping("/{status}/{id}")
    public String showAdjustmentDetailPage(Model model,
                                           @PathVariable String status, @PathVariable Long id,
                                           @RequestParam(value = "adjDate", defaultValue = "", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate adjDate) {

        if (adjDate == null) {
            adjDate = LocalDate.now();
        }

        switch (status) {
            case STATUS_DIV -> {
                // 조회가 division일 경우 보낼 모델
                // divisionService.findById(id);
                // model.addAttribute("detail");

                // id가 아닌 divisionName 으로 변경해야함
                model.addAttribute("viewName", id + "지점");
            }
            case STATUS_TEAM -> {
                // 조회가 team일 경우 보낼 모델
                // teamService.findById(id);
                // model.addAttribute("detail");

                // id가 아닌 teamName 으로 변경해야함
                model.addAttribute("viewName", id + "팀");
            }
        }
        model.addAttribute("currentDate", adjDate);
        log.info("payDate={}", adjDate);
        model.addAttribute("divisionDtoList",
                divisionService.findAllDivision().stream().map(Division::of).toList()
        );
        model.addAttribute("divisionList", divisionService.findAllDivision());
        return "adjustment/detail";
    }
}
