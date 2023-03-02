package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCost;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import com.ezkorea.hybrid_app.service.expenses.FuelCostService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;
    private final FuelCostService fuelCostService;
    private final GasStationService gsService;

    @GetMapping("/expenses/{status}")
    public String showExpensesListPage(Model model, @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                       @AuthenticationPrincipal SecurityUser securityUser, @PathVariable String status) {

        ExpensesStatus currentStatus = ExpensesStatus.valueOf(status);

        model.addAttribute("expensesList", expensesService.findExpensesByMemberAndStatus(securityUser.getMember(), currentStatus, page));

        return "expenses/list";
    }

    @GetMapping("/expenses/create")
    public String showCreateExpensesPage(Model model) {
        FuelCost currentCost = fuelCostService.findFuelCostByLocalDate(LocalDate.now());
        model.addAttribute("stationList", gsService.findAllGasStation());
        model.addAttribute("cost", currentCost);
        return "expenses/create";
    }

}
