package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExpensesRestController {

    private final ExpensesService expensesService;

    @PostMapping("/expenses")
    public ResponseEntity<Object> createExpenses(@RequestBody ExpensesDto dto) {

        Expenses savedExpenses = expensesService.saveExpenses(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", savedExpenses.getId()), HttpStatus.OK);
    }

}
