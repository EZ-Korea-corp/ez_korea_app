package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerRestController {
    private final SaleService saleService;

    @PostMapping("/taskList")
    public Map<String, Object> findTaskList(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", saleService.findTaskDateList(data));

        return returnMap;
    }

    @PostMapping("/totalStat")
    public Map<String, Object> findTotalStat(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = saleService.findTotalStat(data);
        return returnMap;
    }
}
