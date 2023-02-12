package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final MemberService memberService;
    private final GasStationService gasStationService;

    @GetMapping("/sales")
    public String showSalesPage(@AuthenticationPrincipal SecurityUser securityUser,
                                Model model) {

        Map<String, Object> returnMap = saleService.findCurrentTask(securityUser.getMember());

        if (returnMap.size() == 0) {
            model.addAttribute("stations", gasStationService.findAllGasStation());
            return "sales/sales-select";
        }

        model.addAttribute("dailyTask", returnMap);
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

    @GetMapping("/sales/close")
    public String showClosePage() {
        return "sales/close-detail";
    }
}
