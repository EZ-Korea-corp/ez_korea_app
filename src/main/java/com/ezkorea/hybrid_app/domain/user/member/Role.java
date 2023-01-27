package com.ezkorea.hybrid_app.domain.user.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MASTER("대표"),
    MANAGER("경리"),
    LEADER("팀장"),
    EMPLOYEE("직원");

    private final String viewName;

}
