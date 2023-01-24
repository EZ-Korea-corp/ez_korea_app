package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @GetMapping("/sales")
    public String showSalesPage(@AuthenticationPrincipal SecurityUser securityUser) {

        return "sales/sales-detail";
    }

    @GetMapping("/sales/sell")
    public String showSellPage() {
        return "sales/sell-detail";
    }
}
