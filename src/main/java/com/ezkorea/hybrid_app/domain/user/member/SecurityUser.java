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
    private final String name;
    private final String sex;
    private final Role role;
    private final LocalDateTime createDate;

    public SecurityUser(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.sex = member.getSex();
        this.name = member.getName();
        this.username = member.getUsername();
        this.createDate = member.getCreateDate();
        this.role = member.getRole();
    }

    public Member getMember() {
        return Member.builder()
                .id(id)
                .createDate(createDate)
                .name(name)
                .sex(sex)
                .role(role)
                .username(username)
                .build();
    }
}
