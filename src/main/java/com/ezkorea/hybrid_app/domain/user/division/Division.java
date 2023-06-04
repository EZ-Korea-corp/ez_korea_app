package com.ezkorea.hybrid_app.domain.user.division;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Division extends BaseEntity {

    @Setter
    private String divisionName;

    @OneToOne
    @Setter
    private Member leader;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private List<Team> teamList;

    public void makeTeamListInitialization() {
        teamList = new ArrayList<>();
    }

    public DivisionDto of() {
        return DivisionDto.builder()
                .teamName(divisionName)
                .build();
    }
}
