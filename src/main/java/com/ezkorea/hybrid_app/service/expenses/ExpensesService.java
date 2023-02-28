package com.ezkorea.hybrid_app.service.expenses;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesRepository;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;

    public Expenses saveExpenses(ExpensesDto dto) {
        Expenses newExpenses = Expenses.builder()
                .cost(dto.getCost())
                .expensesStatus(dto.getExpensesStatus())
                .payDate(dto.getPayDate())
                .build();
        if (newExpenses.getExpensesStatus().equals(ExpensesStatus.FUEL)) {
            newExpenses.setFuelStatus(dto.getFuelStatus());
        }
        return expensesRepository.save(newExpenses);
    }

}
