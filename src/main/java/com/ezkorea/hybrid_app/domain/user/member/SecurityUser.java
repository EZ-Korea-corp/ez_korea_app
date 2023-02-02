package com.ezkorea.hybrid_app.domain.user.member;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Slf4j
public class SecurityUser extends User {
    private final Long id;
    private final String username;
    private final String email;
    private final String phone;
    private final String name;
    private final String sex;
    private final Role role;
    private final boolean isRoleChanged;
    private final LocalDateTime createDate;

    public SecurityUser(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.name = member.getName();
        this.sex = member.getSex();
        this.role = member.getRole();
        this.isRoleChanged = member.isRoleChanged();
        this.createDate = member.getCreateDate();
    }

    public Member getMember() {
        return Member.builder()
                .id(id)
                .username(username)
                .email(email)
                .phone(phone)
                .name(name)
                .sex(sex)
                .role(role)
                .isRoleChanged(isRoleChanged)
                .createDate(createDate)
                .build();
    }
}
