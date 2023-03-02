package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExpensesRestController {

    private final ExpensesService expensesService;
    private final SaleService saleService;
    private final GasStationService gasStationService;

    @PostMapping("/expenses")
    public ResponseEntity<Object> createExpenses(@RequestBody ExpensesDto dto,
                                                 @AuthenticationPrincipal SecurityUser securityUser) {

        log.info("ExpensesDto dto={}", dto.toString());

        GasStation currentStation = gasStationService.findStationById(dto.getStationId());

        Expenses savedExpenses = expensesService.saveExpenses(dto, securityUser.getMember(), currentStation);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", savedExpenses.getId()), HttpStatus.OK);
    }

    @PutMapping("/expenses")
    public ResponseEntity<Object> checkExpenses(@RequestBody ExpensesDto dto) {

        Expenses currentExpenses = expensesService.checkExpenses(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", currentExpenses.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/expenses")
    public ResponseEntity<Object> deleteExpenses(@RequestBody ExpensesDto dto,
                                                 @AuthenticationPrincipal SecurityUser securityUser) {

        expensesService.deleteExpenses(dto, securityUser.getMember());

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/expenses/station")
    public ResponseEntity<Object> findMemberStationForExpenses(@RequestBody ExpensesDto dto,
                                                               @AuthenticationPrincipal SecurityUser securityUser) {

        List<TimeTable> tableList = saleService.findTableList(dto.getPayDate(), securityUser.getMember());

        if (tableList.size() == 0) {
            return new ResponseEntity<>(Map.of("message", "근무한 이력이 없습니다."), HttpStatus.BAD_REQUEST);
        }

        List<Map<String, String>> stations = new ArrayList<>();
        tableList.forEach(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("stationId", String.valueOf(item.getGasStation().getId()));
            map.put("tableId", String.valueOf(item.getId()));
            map.put("stationName", item.getGasStation().getStationName());
            map.put("stationLocation", item.getGasStation().getStationLocation());
            map.put("part", PartTime.of(item.getPart()));

            stations.add(map);
        });

        return new ResponseEntity<>(Map.of("stations", stations), HttpStatus.OK);
    }


}
