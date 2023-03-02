package com.ezkorea.hybrid_app.domain.wiper;

import com.ezkorea.hybrid_app.domain.sale.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WiperSort {

    NORMAL("normal", "일반", 10000, "일반"),
    HYBRID("hybrid", "하이브리드", 15000, "하브"),
    MW("mw", "엠떠블유(MW)", 20000, "MW"),
    S_CLASS("s_class", "S클래스", 40000, "S클"),
    SIZE_700("size_700", "칠백 사이즈", 15000, "칠백");

    private final String name;
    private final String viewName;
    private final int price;
    private final String init;

    public static String of(String _sort) {
        for (WiperSort sort : WiperSort.values()) {
            if (sort.name.toLowerCase().equals(_sort)) {
                return sort.init;
            }
        }

        return null;
    }
}
