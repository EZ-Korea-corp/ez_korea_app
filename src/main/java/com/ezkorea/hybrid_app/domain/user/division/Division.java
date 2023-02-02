package com.ezkorea.hybrid_app.domain.user.division;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Division extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Enumerated(EnumType.STRING)
    private Position position;

    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;

    public void addBasicInfo(Team team, Member member) {
        this.member = member;
        this.team = team;
    }

}
