package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        List<Map<String, Long>> statList = saleService.findSaleStat(securityUser.getMember(), data);

        returnMap.put("result", statList);

        return returnMap;
    }
}
