package com.ezkorea.hybrid_app.domain.user.division;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {

    LEADER("팀장"),
    SUB_LEADER("부팀장"),
    MEMBER("팀원");

    private final String viewName;

}
