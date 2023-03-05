package com.ezkorea.hybrid_app.domain.user.team;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, exclude = {"division", "leader", "memberList"})
public class Team extends BaseEntity {

    private String teamName;
    @ManyToOne
    private Division division;

    @OneToOne
    private Member leader;

    @OneToMany(mappedBy = "team")
    private List<Member> memberList;
}
