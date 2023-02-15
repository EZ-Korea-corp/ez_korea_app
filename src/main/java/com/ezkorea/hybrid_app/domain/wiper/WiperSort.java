package com.ezkorea.hybrid_app.domain.wiper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WiperSort {

    NORMAL("normal", "일반", 10000),
    HYBRID("hybrid", "하이브리드", 15000),
    MW("mw", "엠떠블유(MW)", 20000),
    S_CLASS("s_class", "S클래스", 40000),
    SIZE_700("size_700", "칠백 사이즈", 15000);

    private final String name;
    private final String viewName;
    private final int price;
}
