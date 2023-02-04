package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.user.member.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpDto {
    private String username;
    private String password;
    private String phone;
    private String email;
    private String sex;
    private String name;
    private boolean isLeader;
    private Role role;
}
