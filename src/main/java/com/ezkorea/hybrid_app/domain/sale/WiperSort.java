package com.ezkorea.hybrid_app.domain.sale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WiperSort {

    NORMAL("normal", "일반", 20000),
    HYBRID("hybrid", "하이브리드", 30000),
    SIZE_700("size700", "700 사이즈", 30000),
    MW("mw", "엠떠블유(MW)", 40000),
    S_CLASS("sclass", "S클래스", 80000);

    private final String name;
    private final String viewName;
    private final int price;
}
