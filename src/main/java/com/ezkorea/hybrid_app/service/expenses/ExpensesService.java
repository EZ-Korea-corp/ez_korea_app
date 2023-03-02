package com.ezkorea.hybrid_app.service.expenses;

import com.ezkorea.hybrid_app.domain.expenses.CheckStatus;
import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesRepository;
import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.service.aws.AWSService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.web.dto.ExpensesDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final AWSService awsService;
    private final GasStationService gsService;

    public Expenses saveExpenses(ExpensesDto dto, Member member) {
        Expenses newExpenses = Expenses.builder()
                .cost(dto.getCost())
                .expensesStatus(dto.getExpensesStatus())
                .checkStatus(CheckStatus.NOT_CHECK)
                .payDate(dto.getPayDate())
                .member(member)
                .build();
        if (newExpenses.getExpensesStatus().equals(ExpensesStatus.FUEL)) {
            newExpenses.setFuelStatus(dto.getFuelStatus());
            newExpenses.setGasStation(gsService.findStationById(dto.getStationId()));
        }
        return expensesRepository.save(newExpenses);
    }

    @Transactional
    public void deleteExpenses(ExpensesDto dto, Member member) {
        Expenses currentExpenses = findExpensesById(dto.getId());
        if (currentExpenses.getMember().getUsername().equals(member.getUsername())) {
            awsService.deleteS3Image(currentExpenses.getS3Image(), true);
            expensesRepository.delete(currentExpenses);
        }
    }

    @Transactional(readOnly = true)
    public Expenses findExpensesById(Long id) {
        return expensesRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "번 경비 내역을 찾을 수 없습니다."));
    }

    public Page<Expenses> findExpensesByMember(Member member, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return expensesRepository.findAllByMemberOrderByPayDateDesc(member, pageable);
    }

    public Page<Expenses> findExpensesByMemberAndStatus(Member member, ExpensesStatus status, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return expensesRepository.findAllByMemberAndExpensesStatusOrderByPayDateDesc(member, status, pageable);
    }

    public Page<Expenses> findAllExpensesByStatus(int page, ExpensesStatus status) {
        Pageable pageable = PageRequest.of(page, 10);
        return expensesRepository.findAllByStatusOrderByPayDateDescManagerCheckAsc(status, pageable);
    }

    public Page<Expenses> findAllExpensesByPayDate(int page, LocalDate date, ExpensesStatus status) {
        Pageable pageable = PageRequest.of(page, 10);
        return expensesRepository.findAllByStatusPayDateOrderByPayDateDescManagerCheckAsc(date, status, pageable);
    }

    @Transactional
    public Expenses checkExpenses(ExpensesDto dto) {
        Expenses currentExpenses = findExpensesById(dto.getId());
        currentExpenses.setCheckStatus(dto.getCheckStatus());
        return currentExpenses;
    }
}
