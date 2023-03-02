package com.ezkorea.hybrid_app.domain.expenses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FuelStatus {
    GASOLINE("휘발유"),
    DIESEL("경유");
    private final String viewName;
}
