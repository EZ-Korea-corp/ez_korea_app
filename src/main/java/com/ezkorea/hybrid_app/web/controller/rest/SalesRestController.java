package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.sale.SaleStatus;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.TaskDto;
import com.ezkorea.hybrid_app.web.dto.TimeTableDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public HttpStatus saveSellsProduct(@RequestBody TimeTableDto timeTableDto) {
        saleService.saveSellProduct(timeTableDto);

        return HttpStatus.OK;
    }

    @PostMapping("/sales/input")
    public HttpStatus saveStockProduct(@RequestBody TimeTableDto timeTableDto) {

        saleService.saveStockProduct(timeTableDto);

        return HttpStatus.OK;
    }

    @PostMapping("/sales/saveIn")
    public HttpStatus saveInputProduct(@RequestBody TimeTableDto timeTableDto,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        if(timeTableDto.getId() != null && timeTableDto.getId() > 0) {
            saleService.updateInputProduct(timeTableDto); //수정
        } else {
            saleService.saveInputProduct(timeTableDto, securityUser.getMember()); // 등록
        }

        return HttpStatus.OK;
    }

    @PostMapping("/sales/input/tableList")
    public Map<String, Object> findInputTableList(@RequestBody Map<String, String> paramMap,
                                                  @AuthenticationPrincipal SecurityUser securityUser) {
        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> list = saleService.findInputTableList(paramMap);

        returnMap.put("list", list);
        return returnMap;
    }

    @PostMapping("/sales/input/list")
    public Map<String, Object> findInputList(@RequestBody Map<String, Long> paramMap) {
        Map<String, Object> returnMap = saleService.findInputList(paramMap);

        return returnMap;
    }

    @PostMapping("/sales/stock/list")
    public Map<String, Object> findStockList(@RequestBody Map<String, Long> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", saleService.findStockList(paramMap));

        return returnMap;
    }

    @PostMapping("/sales/input/delete")
    public HttpStatus deleteInputTable(@RequestBody Map<String, Long> paramMap) {
        saleService.deleteInputTable(paramMap.get("id"));

        return HttpStatus.OK;
    }

    @PostMapping("/sales/close")
    public Map<String, Object> findTableStat(@RequestBody Map<String, String> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        
        if(SaleStatus.OUT.toString().equals(paramMap.get("status"))) {
            // 판매현황
            returnMap = saleService.findTableStat(paramMap); 
        } else {
            // 재고현황
            returnMap.put("stockList", saleService.findStockProduct(paramMap));
        }

        return returnMap;
    }

    @DeleteMapping("/sales/delete")
    public HttpStatus deleteSale(@RequestBody Map<String, Long> data) {

        saleService.deleteSale(data.get("id"));
        return HttpStatus.OK;
    }

    @PostMapping("/timeTable/save")
    public Map<String, Object> saveTimeTable(@RequestBody Map<String, Object> pramMap,
                                        @AuthenticationPrincipal SecurityUser securityUser,
                                        HttpServletResponse response) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();

        Long tTid = saleService.saveTimeTable(pramMap, securityUser.getMember());

        if(tTid.equals(0L)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return returnMap;
        }

        returnMap.put("result", tTid);
        return returnMap;
    }
}
