package com.ezkorea.hybrid_app.domain.user.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    AWAIT("가입대기"),
    FULL_TIME("현직"),
    AWAY("퇴사");
    private final String viewName;

}
