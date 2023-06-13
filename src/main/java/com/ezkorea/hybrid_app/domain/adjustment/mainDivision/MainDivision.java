package com.ezkorea.hybrid_app.domain.adjustment.mainDivision;

import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainDivision {
    HEAD_OFFICE("head", "본사"),
    CHUNG_CHEONG("chungcheong", "충청도"),
    JEOLLA("jeolla", "전라도"),
    GYEONG_SANG("gyeongsang", "경상도"),
    DAEGU("daegu", "대구");

    private final String key;
    private final String value;

    public static String of(String part) {
        for (MainDivision div : MainDivision.values()) {
            if (div.key.toLowerCase().equals(part)) {
                return div.value;
            }
        }

        return null;
    }
}
