package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.sale.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_MASTER("ROLE_MASTER", "대표"),
    ROLE_DIRECTOR("ROLE_DIRECTOR","이사"),
    ROLE_MANAGER("ROLE_MANAGER","경리"),
    ROLE_GM("ROLE_GM","지점장"),
    ROLE_LEADER("ROLE_LEADER","팀장"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE","사원");

    private final String key;
    private final String viewName;

    public static String of(String _role) {
        for (Role role : Role.values()) {
            if (role.key.equals(_role)) {
                return role.viewName;
            }
        }

        return "";
    }

}
