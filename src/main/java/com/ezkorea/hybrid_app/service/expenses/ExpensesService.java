package com.ezkorea.hybrid_app.service.expenses;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesRepository;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;

    public Expenses saveExpenses(ExpensesDto dto, Member member) {
        Expenses newExpenses = Expenses.builder()
                .cost(dto.getCost())
                .expensesStatus(dto.getExpensesStatus())
                .payDate(dto.getPayDate())
                .member(member)
                .build();
        if (newExpenses.getExpensesStatus().equals(ExpensesStatus.FUEL)) {
            newExpenses.setFuelStatus(dto.getFuelStatus());
        }
        return expensesRepository.save(newExpenses);
    }

    public List<Expenses> findExpensesByMember(Member member) {
        return expensesRepository.findAllByMember(member);
    }

}
