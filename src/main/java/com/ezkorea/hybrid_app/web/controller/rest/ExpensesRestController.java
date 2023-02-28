package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExpensesRestController {

    private final ExpensesService expensesService;

    @PostMapping("/expenses")
    public ResponseEntity<Object> createExpenses(@RequestBody ExpensesDto dto,
                                                 @AuthenticationPrincipal SecurityUser securityUser) {

        Expenses savedExpenses = expensesService.saveExpenses(dto, securityUser.getMember());

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

}
