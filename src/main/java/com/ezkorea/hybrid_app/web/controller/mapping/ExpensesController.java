package com.ezkorea.hybrid_app.web.controller.mapping;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExpensesController {

    private final ExpensesService expensesService;

    @GetMapping("/expenses")
    public String showExpensesListPage(Model model, @RequestParam(value="page", defaultValue="0", required = false) int page,
                                       @AuthenticationPrincipal SecurityUser securityUser) {

        model.addAttribute("expensesList", expensesService.findExpensesByMember(securityUser.getMember(), page));

        return "expenses/list";
    }

    @GetMapping("/expenses/create")
    public String showCreateExpensesPage() {
        return "expenses/create";
    }

}
