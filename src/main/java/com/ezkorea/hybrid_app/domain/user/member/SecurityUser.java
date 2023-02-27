package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Slf4j
public class SecurityUser extends User implements UserDetails {
    private final Long id;
    private final String username;
    private final String email;
    private final String phone;
    private final String name;
    private final String sex;
    private final Role role;
    private final MemberStatus memberStatus;
    private final boolean isRoleChanged;
    private final LocalDateTime createDate;
    private final SubAuth subAuth;
    private final S3Image s3Image;

    public SecurityUser(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.name = member.getName();
        this.sex = member.getSex();
        this.role = member.getRole();
        this.memberStatus = member.getMemberStatus();
        this.isRoleChanged = member.isRoleChanged();
        this.createDate = member.getCreateDate();
        this.subAuth = member.getSubAuth();
        this.s3Image = member.getS3Image();
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
                .memberStatus(memberStatus)
                .isRoleChanged(isRoleChanged)
                .createDate(createDate)
                .subAuth(subAuth)
                .s3Image(s3Image)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
