package com.ezkorea.hybrid_app.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class MemberDto {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String role;
    private String division;
    private String team;
    private String profileImgUrl;

}
