package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final MemberRepository memberRepository;
    private final SaleService saleService;
    private final MemberService memberService;
    private final GasStationService gasStationService;

    @GetMapping("/sales")
    public String showSalesPage(@AuthenticationPrincipal SecurityUser securityUser,
                                Model model) {

        Member memeber = memberService.findMemberById(securityUser.getMember().getId());

        if (memeber.getTaskList().size() == 0) return "redirect:/sales/select";

        List<GasStation> stations = new ArrayList<>();
        memeber.getTaskList().forEach(item ->
            stations.add(item.getGasStation())
        );
        model.addAttribute("stations", stations);
        return "sales/sales-selectWork";
    }

    @GetMapping("/sales/select")
    public String showSalesSelectPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

        model.addAttribute("stations", gasStationService.findAllGasStation());
        return "sales/sales-select";
    }

    @GetMapping("/sales/index/{id}")
    public String showSellPage(@AuthenticationPrincipal SecurityUser securityUser,
                               @PathVariable Long id,
                               Model model) {
        Map<String, Object> returnMap = saleService.findCurrentTask(securityUser.getMember(), id);
        model.addAttribute("dailyTask", returnMap);
        return "sales/sales-detail";
    }

    @GetMapping("/sales/sell/{id}")
    public String showSellPage(@PathVariable Long id, Model model) {
        model.addAttribute("stationId", id);
        return "sales/sell-detail";
    }

    @GetMapping("/sales/input/{id}")
    public String showInputPage(@PathVariable Long id, Model model) {
        model.addAttribute("stationId", id);
        return "sales/in-detail";
    }

    @GetMapping("/sales/close/{id}")
    public String showClosePage(@PathVariable Long id, Model model) {
        model.addAttribute("stationId", id);
        return "sales/close-detail";
    }
}
