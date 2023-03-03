package com.ezkorea.hybrid_app.domain.expenses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CheckStatus {

    CHECK("확인"),
    AWAIT("보류"),
    NOT_CHECK("미확인");
    private final String viewName;

}
