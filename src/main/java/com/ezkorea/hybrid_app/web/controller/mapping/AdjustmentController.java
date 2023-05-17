package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/adj")
public class AdjustmentController {
    private final DivisionService divisionService;
    @GetMapping("/main")
    public String showAdjustmentPage(Model model,
                                     @RequestParam(value="adjDate", defaultValue="", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate payDate) {
        log.info("payDate={}", payDate);
        model.addAttribute("divisionDtoList",
                divisionService.findAllDivision().stream().map(Division::of).toList()
        );
        model.addAttribute("divisionList", divisionService.findAllDivision());
        return "adjustment/main";
    }
}
