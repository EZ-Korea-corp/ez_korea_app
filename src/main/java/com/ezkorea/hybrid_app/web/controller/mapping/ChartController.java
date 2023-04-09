package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController {
    private final CommuteService commuteService;
    @GetMapping("/view")
    public String showMemberChart(Model model) {
        // 조직도 조회
        model.addAttribute("profileList", commuteService.findMemberChart());
        return "profile/chart";
    }

}
