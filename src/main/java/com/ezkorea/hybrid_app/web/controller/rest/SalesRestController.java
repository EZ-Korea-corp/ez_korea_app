package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
