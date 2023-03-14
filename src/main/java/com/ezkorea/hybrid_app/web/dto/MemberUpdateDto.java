package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MemberUpdateDto {
    private String username;
    private Role memberRole;
    private MemberStatus memberStatus;
    // 글쓰기 권한
    private String memberPostAuth;
    // 입고 권한
    private String memberInputAuth;
}
