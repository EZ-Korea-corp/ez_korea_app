package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/station")
@RequiredArgsConstructor
@Slf4j
public class GasStationController {

    private final SaleService saleService;
    private final GasStationService gasStationService;

    @GetMapping("/index")
    public String showGasStationListPage(Model model) {
        model.addAttribute("stationList", gasStationService.findOnGasStation());
        return "gasStation/gasStation-index";
    }

    @GetMapping("/indexAll")
    public String showGasStationAllListPage(Model model) {
        model.addAttribute("stationList", gasStationService.findAllGasStation());
        model.addAttribute("isAll", true);
        return "gasStation/gasStation-index";
    }

    @GetMapping("/detail/{id}")
    public String showGasStationDetailPage(@PathVariable Long id, Model model) {
        GasStation station = gasStationService.findStationById(id);

        model.addAttribute("station", station);
        return "gasStation/gasStation-detail";
    }

    @GetMapping("/update/{id}")
    public String showGasStationUpdatePage(@PathVariable Long id, Model model) {
        GasStation station = gasStationService.findStationById(id);

        model.addAttribute("station", station);
        return "gasStation/gasStation-update";
    }

    @GetMapping("/save")
    public String saveGasStationPage() {
        return "gasStation/gasStation-save";
    }

    @GetMapping("/inList")
    public String showInListPage(@RequestParam(value="id") String id,
                                 @RequestParam(value="date") String date,
                                 Model model) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id); //주유소아이디
        paramMap.put("date", date);

        model.addAttribute("inList", saleService.findInList(paramMap));

        return "gasStation/gasStation-inList";
    }

    @GetMapping("/inDetail")
    public String showSaleInHistoryPage() {

        return "gasStation/gasStation-inDetail";
    }

    @GetMapping("/outDetail")
    public String showSaleOutHistoryPage(@RequestParam(value="id") String id,
                                         @RequestParam(value="date") String date,
                                         Model model) {
        Map<String, String> paramMap = new HashMap<>();
        log.info("paramMap={}", paramMap);
        paramMap.put("id", id);
        paramMap.put("date", date);

        model.addAttribute("outList", saleService.findSellDetailByStationAndDate(paramMap));
        model.addAttribute("fixList", saleService.findFixDetailByStationAndDate(paramMap));

        return "gasStation/gasStation-outDetail";
    }

    @GetMapping("/stockList")
    public String showStockListPage(@RequestParam(value="id") String id,
                                    @RequestParam(value="date") String date,
                                    Model model) {

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("id", id); //주유소아이디
        paramMap.put("date", date);

        model.addAttribute("stockList", saleService.findNotInList(paramMap));

        return "gasStation/gasStation-stockList";
    }

    @GetMapping("/stockDetail")
    public String showSaleStockHistoryPage() {

        return "gasStation/gasStation-stockDetail";
    }

}