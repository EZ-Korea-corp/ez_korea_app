package com.ezkorea.hybrid_app.domain.sale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleStatus {

    IN("in"),
    OUT("out"),
    FIX("fix"),
    STOCK("stock");

    private final String viewName;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}