package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Payment {

    CARD("card", "카드"),
    CASH("cash", "현금");

    private final String key;
    private final String viewName;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static String of(String _payment) {
        for (Payment payment : Payment.values()) {
            if (payment.key.toLowerCase().equals(_payment)) {
                return payment.viewName;
            }
        }

        return null;
    }
}
