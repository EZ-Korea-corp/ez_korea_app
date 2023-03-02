package com.ezkorea.hybrid_app.domain.timetable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PartTime {

    AM("am", "오전"),
    PM("pm", "오후"),
    FULL("full", "풀타임"),
    IN("in", "입고");

    private final String key;
    private final String viewName;

    public static String of(String part) {
        for (PartTime partTime : PartTime.values()) {
            if (partTime.key.toLowerCase().equals(part)) {
                return partTime.viewName;
            }
        }

        return null;
    }

}
