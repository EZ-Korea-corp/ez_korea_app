package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.sale.SaleStatus;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final GasStationService gasStationService;

    @GetMapping("/sales")
    public String showSalesPage(@AuthenticationPrincipal SecurityUser securityUser,
                                Model model) {

        List<TimeTable> tableList = saleService.findTableList(LocalDate.now(), securityUser.getMember());

        if(tableList.size() == 0) return "redirect:/sales/select";

        List<Map<String, String>> stations = new ArrayList<>();
        tableList.forEach(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("tableId", String.valueOf(item.getId()));
            map.put("stationName", item.getGasStation().getStationName());
            map.put("stationLocation", item.getGasStation().getStationLocation());
            map.put("part", PartTime.of(item.getPart()));

            stations.add(map);
        });

        model.addAttribute("stations", stations);
        return "sales/sales-selectWork";
    }

    @GetMapping("/sales/select")
    public String showSalesSelectPage(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

        model.addAttribute("stations", gasStationService.findOnGasStation());
        return "sales/sales-select";
    }

    @GetMapping("/sales/index/{id}")
    public String showSellIndexPage(@PathVariable Long id, Model model) {
        Map<String, Object> returnMap = saleService.findTimeTable(id);
        returnMap.put("tTid", id);

        model.addAttribute("map", returnMap);
        return "sales/sales-detail";
    }

    @GetMapping("/sales/sell/{id}")
    public String showSellPage(@PathVariable Long id, Model model) {
        model.addAttribute("tTid", id);
        return "sales/sell-detail";
    }

    @GetMapping("/sales/input/index")
    public String showInputIndexPage() {
        return "sales/in-index";
    }

    @GetMapping("/sales/input/save")
    public String showInputSavePage(@RequestParam(value="id", required=false)Long id, Model model) {
        model.addAttribute("id", id);

        if(id == null) {
            model.addAttribute("stations", gasStationService.findOnGasStation());
        } else {
            model.addAttribute("stations", gasStationService.findAllGasStation());
        }

        // 본인 입고 조회
        return "sales/in-detail";
    }


    @GetMapping("/sales/close/{id}")
    public String showClosePage(@PathVariable Long id, Model model) {
        model.addAttribute("tTid", id);
        return "sales/close-detail";
    }
}
