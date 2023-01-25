package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SalesService;
import com.ezkorea.hybrid_app.web.dto.SellDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SalesRestController {
    private final SalesService salesService;

    @PostMapping("/sales/sell")
    public HttpStatus saveSalesProduct(@RequestBody SellDto sellDto,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        salesService.saveSaleProduct(sellDto, securityUser.getMember());

        return HttpStatus.OK;
    }
}
