package com.ezkorea.hybrid_app.domain.timetable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartTime {

    AM("am", "오전", 200000),
    PM("pm", "오후", 300000),
    FULL("full", "풀타임", 500000),
    IN("in", "입고", 0);

    private final String key;
    private final String viewName;
    private final long minPrice;

    public static String of(String part) {
        for (PartTime partTime : PartTime.values()) {
            if (partTime.key.toLowerCase().equals(part)) {
                return partTime.viewName;
            }
        }

        return null;
    }

}
