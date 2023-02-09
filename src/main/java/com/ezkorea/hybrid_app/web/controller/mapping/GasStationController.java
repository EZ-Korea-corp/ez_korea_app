package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.service.etc.AttachService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/station")
@RequiredArgsConstructor
@Slf4j
public class GasStationController {

    private final SaleService saleService;

    private final GasStationService gasStationService;
    private final AttachService attachService;

    @GetMapping("/index")
    public String showGasStationListPage(Model model) {
        model.addAttribute("stationList", gasStationService.findAllGasStation());
        return "gasStation/gasStation-index";
    }

    @GetMapping("/detail/{id}")
    public String showGasStationDetailPage(@PathVariable Long id, Model model) {
        GasStation station = gasStationService.findStationById(id);

        model.addAttribute("station", station);
        model.addAttribute("attachList", attachService.findAttachList("station" + station.getId()));
        return "gasStation/gasStation-detail";
    }

    @GetMapping("/stock/{id}")
    public String showStockHistoryPage(@PathVariable Long id, Model model) {
        model.addAttribute("station"  , gasStationService.findStationById(id));
        model.addAttribute("stockList", saleService.findStockHistory(id));
        
        // if(stockList.size() == 0) 재고기록 없을때 상세페이지 리다이렉트 처리

        return "gasStation/gasStation-stock";
    }

    @GetMapping("/save")
    public String saveGasStation(@RequestParam(value="id", required=false)Long id, Model model) {
        return "gasStation/gasStation-save";
    }
}