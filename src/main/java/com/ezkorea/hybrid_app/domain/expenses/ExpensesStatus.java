package com.ezkorea.hybrid_app.domain.expenses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpensesStatus {

    FUEL("주유비"),
    FOOD("식비");
    private final String viewName;

}
