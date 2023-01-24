package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SalesRestController {
    private final SalesService salesService;

    @PutMapping("/sales/{id}")
    public void saveSalesProduct(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {

    }
}
