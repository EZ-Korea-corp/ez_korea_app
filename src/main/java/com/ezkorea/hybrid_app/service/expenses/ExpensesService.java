package com.ezkorea.hybrid_app.service.expenses;

import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesRepository;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Expenses> findExpensesByMember(Member member, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return expensesRepository.findAllByMemberOrderByPayDateDesc(member, pageable);
    }

}
