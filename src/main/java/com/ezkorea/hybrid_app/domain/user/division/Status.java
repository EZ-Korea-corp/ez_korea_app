package com.ezkorea.hybrid_app.domain.user.division;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    COMPLETE("승인"),
    AWAIT("대기"),
    DENY("거절");

    private final String viewName;

}
