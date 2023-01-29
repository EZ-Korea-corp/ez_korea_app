package com.ezkorea.hybrid_app.domain.user.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_MASTER("대표"),
    ROLE_MANAGER("경리"),
    ROLE_LEADER("팀장"),
    ROLE_EMPLOYEE("직원");

    private final String viewName;

}
