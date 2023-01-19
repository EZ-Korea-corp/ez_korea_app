package com.ezkorea.hybrid_app.domain.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SecurityUser extends User {
    private final Long id;
    private final String username;
    private final LocalDateTime createDate;

    public SecurityUser(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.username = member.getUsername();
        this.createDate = member.getCreateDate();
    }

    public Member getMember() {
        return Member.builder()
                .id(id)
                .createDate(createDate)
                .username(username)
                .build();
    }
}
