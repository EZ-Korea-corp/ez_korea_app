package com.ezkorea.hybrid_app.domain.expenses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpensesStatus {

    FOOD("식비"),
    FUEL("주유비"),
    ETC("기타");
    private final String viewName;

}
