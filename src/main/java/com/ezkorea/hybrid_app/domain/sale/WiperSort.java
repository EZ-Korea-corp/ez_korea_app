package com.ezkorea.hybrid_app.domain.sale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WiperSort {

    NORMAL("일반", 20000),
    HYBRID("하브", 30000),
    MW("MW", 40000),
    S_CLASS("S-class", 80000);

    private final String name;
    private final int price;
}
