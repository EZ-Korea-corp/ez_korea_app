package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.etc.AttachService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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

    @GetMapping("/save")
    public String saveGasStationPage() {
        return "gasStation/gasStation-save";
    }

    @GetMapping("/inoutList")
    public String showInoutListPage(@RequestParam(value="id") Long id,
                                    @RequestParam(value="date") String date,
                                    Model model) {

        model.addAttribute("inoutList", saleService.findInoutList(id, date));

        return "gasStation/gasStation-inoutList";
    }

    @GetMapping("/inOutDetail")
    public String showSaleInHistoryPage(@RequestParam(value="id") Long id,
                                        @AuthenticationPrincipal SecurityUser securityUser,
                                        Model model) {

        List<Map<String, Object>> list = saleService.findInProductList(securityUser.getMember(), id);
        model.addAttribute("list", list);

        return "gasStation/gasStation-inoutDetail";
    }

    @GetMapping("/inOutMemberDetail")
    public String showSaleInOutHistoryPage(@RequestParam(value="id") Long id,
                                        Model model) {

        List<Map<String, Object>> list = saleService.findInOutProductList(id);
        model.addAttribute("list", list);

        return "gasStation/gasStation-inoutDetail";
    }


    @GetMapping("/out")
    public String showSaleOutHistoryPage() {
        return "gasStation/gasStation-out";
    }

    @GetMapping("/fix")
    public String showSaleFixHistoryPage() {
        return "gasStation/gasStation-fix";
    }

    @GetMapping("/stock")
    public String showStockHistoryPage() {
        return "gasStation/gasStation-stock";
    }

    @GetMapping("/withdraw")
    public String showWithdrawHistoryPage() {
        return "gasStation/gasStation-end";
    }
}