package com.ezkorea.hybrid_app.web.dto;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DivisionDto {
    private String teamName;
    private Member teamGm;
}
