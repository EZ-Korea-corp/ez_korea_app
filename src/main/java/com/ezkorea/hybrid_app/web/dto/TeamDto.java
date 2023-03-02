package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class TeamDto {

    private Division division;
    private String teamName;
    private Member leader;
    private List<Member> memberList;
}
