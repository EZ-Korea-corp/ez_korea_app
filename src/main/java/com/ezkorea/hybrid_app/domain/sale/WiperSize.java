package com.ezkorea.hybrid_app.domain.sale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WiperSize {

    SIZE_300("300"),
    SIZE_350("350"),
    SIZE_400("400"),
    SIZE_450("450"),
    SIZE_500("500"),
    SIZE_550("550"),
    SIZE_600("600"),
    SIZE_650("650"),
    SIZE_700("700");

    private final String name;
}
