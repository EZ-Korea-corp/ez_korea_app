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

    @OneToOne(targetEntity = Team.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Team team;

    @OneToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
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
