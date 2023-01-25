package com.ezkorea.hybrid_app.domain.account.commute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommuteStatus {
    ON_TIME("onTime", "출근"),
    OFF_TIME("offTime", "퇴근"),
    AWAY("away", "대기");

    private final String key;
    private final String value;
}
