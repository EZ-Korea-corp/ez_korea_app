package com.ezkorea.hybrid_app.web.controller.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ExpensesController {

    @GetMapping("/expenses")
    public String showCreateExpensesPage() {
        return "expenses/create";
    }

}
