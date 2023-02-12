package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SalesRestController {
    private final SaleService saleService;

    @PostMapping("/sales/sell")
    public HttpStatus saveSalesProduct(@RequestBody WiperDto wiperDto,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        saleService.saveSaleProduct(wiperDto, securityUser.getMember());

        return HttpStatus.OK;
    }

    @PostMapping("/sales/select")
    public HttpStatus saveTeam(@RequestBody Map<String, Object> data,
                               @AuthenticationPrincipal SecurityUser securityUser) {

        saleService.saveDailyGasStation((String) data.get("stationName"), securityUser.getMember());


        return HttpStatus.OK;
    }

    @PostMapping("/sales/findInput")
    public Map<String, Object> findInputProduct(@AuthenticationPrincipal SecurityUser securityUser) {
        Map<String, Object> returnMap = new HashMap<>();
        List<SaleProductDto> dtolist  = new ArrayList<>();
        List<SaleProduct> inputList   = saleService.findInputProduct(securityUser.getMember());

        inputList.forEach(item -> {
            SaleProductDto dto = new SaleProductDto();
            dto.setWiper(item.getWiper().getId());
            dto.setCount(item.getCount());

            dtolist.add(dto);
        });

        returnMap.put("result", dtolist);

        return returnMap;
    }

    @PostMapping("/sales/input")
    public HttpStatus saveInputProduct(@RequestBody List<SaleProductDto> data,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        saleService.saveInputProduct(securityUser.getMember(), data);

        return HttpStatus.OK;
    }

    @PostMapping("/sales/close")
    public Map<String, Object> findSaleStat(@RequestBody Map<String, Object> data,
                                   @AuthenticationPrincipal SecurityUser securityUser) {
        Map<String, Object> returnMap = new HashMap<>();
        List<SaleProductDto> statList = saleService.findSaleStat(securityUser.getMember(), data);

        returnMap.put("result", statList);

        return returnMap;
    }

    @PostMapping("/sales/closeTask")
    public HttpStatus closeTask(@AuthenticationPrincipal SecurityUser securityUser) {

        //마감처리 - 퇴근처리

        return HttpStatus.OK;
    }

    @DeleteMapping("/sales/delete")
    public HttpStatus deleteSale(@RequestBody Map<String, Long> data) {

        saleService.deleteSale(data.get("id"));
        return HttpStatus.OK;
    }

    @PostMapping("/stock/hisoty")
    public Map<String, Object> findStockHistory(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("result", saleService.findStockHistory(paramMap));

        return returnMap;
    }

    @PostMapping("/inout/hisoty")
    public Map<String, Object> findInOutDetail(@RequestBody Map<String, Object> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("result", saleService.findInOutDetail(paramMap));

        return returnMap;
    }

    @PostMapping("/station/withdraw")
    public Map<String, Object> saveWithdraw(@RequestBody Map<String, Object> paramMap, @AuthenticationPrincipal SecurityUser securityUser) {
        Map<String, Object> returnMap = new HashMap<>();
        DailyTask currentTask = saleService.findByMemberAndDate(securityUser.getMember());

        saleService.deleteByTaskAndStatus(currentTask); // 삭제 -Transaction 분리
        saleService.saveWithdraw(paramMap, securityUser.getMember(), currentTask); // 저장

        return returnMap;
    }

    @PostMapping("/station/lastWithdraw")
    public Map<String, Object> findLastWithdraw(@RequestBody Map<String, Object> paramMap, @AuthenticationPrincipal SecurityUser securityUser) {
        Map<String, Object> returnMap = new HashMap<>();

        saleService.findLastWithdraw(paramMap, returnMap);

        return returnMap;
    }


}
