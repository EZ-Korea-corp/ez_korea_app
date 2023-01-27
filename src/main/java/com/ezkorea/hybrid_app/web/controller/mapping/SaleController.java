package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @GetMapping("/sales")
    public String showSalesPage(@AuthenticationPrincipal SecurityUser securityUser,
                                Model model) {

        DailyTask currentTask = saleService.findByMemberAndDate(securityUser.getMember());

        if (currentTask.getGasStation() == null) {
            model.addAttribute("stations", saleService.findAllGasStation());
            return "sales/sales-select";
        }

        model.addAttribute("dailyTask", currentTask);
        return "sales/sales-detail";
    }

    @GetMapping("/sales/sell")
    public String showSellPage() {
        return "sales/sell-detail";
    }

    @GetMapping("/sales/input")
    public String showInputPage() {
        return "sales/in-detail";
    }
}