package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.expenses.ExpensesStatus;
import com.ezkorea.hybrid_app.domain.expenses.FuelStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ExpensesDto {

    private Long id;
    private int cost;

    private LocalDate payDate;

    private ExpensesStatus expensesStatus;

    private FuelStatus fuelStatus;
}
