package com.ezkorea.hybrid_app.domain.user.team;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Team extends BaseEntity {

    private String name;

    @OneToOne
    @Setter
    private Division division;

}
