package com.ezkorea.hybrid_app.web.controller.mapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SalesController {
    @GetMapping("/sales/{id}")
    public String showSalesPage(@PathVariable Long id) {
        return "product/detail";
    }
}
